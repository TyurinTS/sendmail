package com.vaadindemo.vaadindemo.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadindemo.vaadindemo.email.SimpleEmailSender;
import com.vaadindemo.vaadindemo.entities.PersonData;
import com.vaadindemo.vaadindemo.repositories.PersonRepository;
import org.apache.commons.lang3.StringUtils;

@Route
public class MainView extends VerticalLayout {

    private final PersonRepository repository;
    private final SimpleEmailSender emailSender;
    private final MainEditor editor;

    private final Grid<PersonData> grid = new Grid<>(PersonData.class);
    private final TextField filterTextField = new TextField();
    private final Button newPersonButton = new Button("New customer", VaadinIcon.PLUS.create());
    private final Button sendEmailButton = new Button("Send Email");;

    MainView(PersonRepository repository, SimpleEmailSender emailSender, MainEditor editor) {
        this.repository = repository;
        this.emailSender = emailSender;
        this.editor = editor;

        HorizontalLayout actions = new HorizontalLayout(filterTextField, newPersonButton, sendEmailButton);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "eMail", "name");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterTextField.addValueChangeListener(e -> listPerson(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editPersonData(e.getValue());
        });

        newPersonButton.addClickListener(e -> editor.editPersonData(new PersonData("", "")));
        sendEmailButton.addClickListener(e -> emailSender.SendSimpleEmail(repository.findAll()));

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

}
