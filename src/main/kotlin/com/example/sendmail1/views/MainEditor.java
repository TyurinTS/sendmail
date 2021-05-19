package com.example.sendmail1.views;

import com.example.sendmail1.datasrv.PersonData;
import com.example.sendmail1.datasrv.PersonDataRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import javax.annotation.PostConstruct;

@UIScope
@SpringComponent
public class MainEditor extends VerticalLayout implements KeyNotifier {
    private final PersonDataRepository repository;
    private PersonData personData;

    // Внимание: имена компонентов должны быть точно такие же, как в Entity. Нужно для связывания
    TextField eMail = new TextField("EMail");
    TextField name = new TextField("Name");

    Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
    Button cancelButton = new Button("Cancel");
    Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actionsLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);

    Binder<PersonData> binder = new Binder<>(PersonData.class);
    private ChangeHandler changeHandler;

    MainEditor(PersonDataRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initEditor() {
        add(eMail, name, actionsLayout);
        binder.bindInstanceFields(this);
        setSpacing(true);
        saveButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        saveButton.addClickListener(e -> save());
        deleteButton.addClickListener(e -> delete());
        cancelButton.addClickListener(e -> editPersonData(personData));
        setVisible(false);
    }

    interface ChangeHandler {
        void onChange();
    }

    void delete() {
        repository.delete(personData);
        changeHandler.onChange();
    }

    void save() {
        repository.save(personData);
        changeHandler.onChange();
    }

    public final void editPersonData(PersonData p) {
        if (p == null) {
            setVisible(false);
            return;
        }

        final boolean persisted = p.getId() != null;
        if (persisted) {
            personData = repository.findById(p.getId()).get();
        } else {
            personData = p;
        }

        cancelButton.setVisible(persisted);
        binder.setBean(personData);
        setVisible(true);
        eMail.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
