package com.example.notemanegersystem.repository;

import com.example.notemanegersystem.entity.ConfirmEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmEmailRepository extends JpaRepository<ConfirmEmail, Integer> {
    ConfirmEmail findConfirmEmailByCode(String code);
}
