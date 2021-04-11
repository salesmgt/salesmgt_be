package com.app.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.app.demo.models.PersonalActivity;

@Repository
public interface PersonalActivityRepository extends JpaRepository<PersonalActivity, Integer>,JpaSpecificationExecutor<PersonalActivity> {
  List<PersonalActivity> findByRecurrenceID(int recurrenceID);
}
