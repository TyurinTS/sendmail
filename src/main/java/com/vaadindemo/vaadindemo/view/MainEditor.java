package com.vaadindemo.vaadindemo.view;

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
import com.vaadindemo.vaadindemo.entities.PersonData;
import com.vaadindemo.vaadindemo.repositories.PersonRepository;

@SpringComponent
@UIScope
public class MainEditor extends VerticalLayout implements KeyNotifier {

    private final PersonRepository repository;
    private PersonData personData;

    TextField eMail = new TextField("EMail");
    TextField name = new TextField("Name");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<PersonData> binder = new Binder<>(PersonData.class);
    private ChangeHandler changeHandler;

    MainEditor(PersonRepository repository) {
        this.repository = repository;
        add(eMail, name, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editPersonData(personData));
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

        cancel.setVisible(persisted);
        binder.setBean(personData);
        setVisible(true);
        eMail.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
