package com.example.sendmail1.datasrv;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PersonData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Внимание: имена полей должны быть точно такие же, как в MainEditor. Нужно для связывания (binding)
    private String eMail;
    private String name;
    private boolean isScuccess;

    protected PersonData() {}

    public PersonData(String eMail, String name) {
        this.eMail = eMail;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isScuccess() {
        return isScuccess;
    }

    public void setScuccess(boolean scuccess) {
        isScuccess = scuccess;
    }
}
