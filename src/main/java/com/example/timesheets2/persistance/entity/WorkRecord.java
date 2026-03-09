package com.example.timesheets2.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "WorkRecord")
@Getter
@Setter
@EqualsAndHashCode
public class WorkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "record_date")
    LocalDate date;

    Integer minutes;

    String description;

    @Column(name = "project_id")
    Long projectId;

    @Column(name = "user_id")
    String userId;

}
