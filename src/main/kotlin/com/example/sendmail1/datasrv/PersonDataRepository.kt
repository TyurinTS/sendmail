package com.example.sendmail1.datasrv

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonDataRepository: CrudRepository<PersonData, Long> {
    override fun findAll(): List<PersonData>
}