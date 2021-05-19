package com.example.sendmail1.views

import com.example.sendmail1.datasrv.PersonData
import com.example.sendmail1.datasrv.PersonDataRepository
import com.example.sendmail1.emailsrv.EmailService
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.Route
import org.thymeleaf.util.StringUtils
import javax.annotation.PostConstruct

@Route
class MainView(
    val repository: PersonDataRepository,
    val emailService: EmailService,
    val editor: MainEditor
): VerticalLayout() {
    val grid = Grid(PersonData::class.java)
    val filterTextField = TextField()
    val newPersonButton = Button("New customer", VaadinIcon.PLUS.create())
    val sendEmailButton = Button("Send Email", VaadinIcon.MAILBOX.create())

    @PostConstruct
    fun initView() {
        val actions = HorizontalLayout(filterTextField, newPersonButton, sendEmailButton);

        add(actions, grid, editor)

        grid.height = "300px"
        grid.setColumns("id", "eMail", "name")
        grid.getColumnByKey("id").setWidth("50px").flexGrow = 0

        filterTextField.valueChangeMode = ValueChangeMode.EAGER
        filterTextField.addValueChangeListener { l -> listPerson(l.value) }

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener { l -> editor.editPersonData(l.value) }

        newPersonButton.addClickListener { l -> editor.editPersonData(PersonData("", "")) }
        sendEmailButton.addClickListener { l -> sendEm }

    }

    fun listPerson(filterText: String) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repository.findAll())
        }
    }
}