package com.vaadindemo.vaadindemo.repositories;

import com.vaadindemo.vaadindemo.entities.PersonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonData, Long> {
    List<PersonData> findAll();
}
