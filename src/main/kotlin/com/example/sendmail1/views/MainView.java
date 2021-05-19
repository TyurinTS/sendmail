package com.example.sendmail1.views;

import com.example.sendmail1.datasrv.PersonData;
import com.example.sendmail1.datasrv.PersonDataRepository;
import com.example.sendmail1.emailsrv.EmailContext;
import com.example.sendmail1.emailsrv.EmailService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route
public class MainView extends VerticalLayout {
    private PersonDataRepository repository;
    private EmailService emailService;
    private MainEditor editor;

    private final Grid<PersonData> grid = new Grid<>(PersonData.class);
    private final TextField filterTextField = new TextField();
    private final Button newPersonButton = new Button("New customer", VaadinIcon.PLUS.create());
    private final Button sendEmailButton = new Button("Send Email");

    public MainView(PersonDataRepository repository, EmailService emailService, MainEditor editor) {
        this.repository = repository;
        this.emailService = emailService;
        this.editor = editor;
    }

    @PostConstruct
    public void initView() {
        HorizontalLayout actions = new HorizontalLayout(filterTextField, newPersonButton, sendEmailButton);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "eMail", "name");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterTextField.addValueChangeListener(e -> listPerson(e.getValue()));

//         Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editPersonData(e.getValue());
        });

        newPersonButton.addClickListener(e -> editor.editPersonData(new PersonData("", "")));
        sendEmailButton.addClickListener(e -> sendEmail(repository.findAll()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listPerson(filterTextField.getValue());
        });



        // Initialize listing
        listPerson(null);

//        repository.save(new PersonData("timt@mail.ru", "Tim"));
//        grid.removeColumnByKey("id");
//        add(grid);
//        grid.setItems(repository.findAll());
    }

    void listPerson(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repository.findAll());
        }
//        else {
//            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
//        }
    }

    void sendEmail(List<PersonData> p) {
        repository.save(new PersonData("wer", "wer"));
        EmailContext emailContext = new EmailContext();
//        emailContext.setTemplateLocation("/home/tim/develop/IdeaProject/vaadin-crud/src/main/resources/template/message.html");
        emailContext.setFrom("tim.tyurin@gmail.com");
        emailContext.setSubject("Вакансия");

        p.parallelStream().forEach(l -> {
            emailContext.setTo(l.geteMail());
            Map<String, Object> context = new HashMap<>();
            context.put("name", l.getName());
            emailContext.setContext(context);
            emailContext.setTemplateLocation("message-template");

            try {
                emailService.sendMail(emailContext);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }
}
