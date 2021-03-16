package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.PersonalActivity;

@Repository
public interface PersonalActivityRepository extends JpaRepository<PersonalActivity, Integer> {

}
