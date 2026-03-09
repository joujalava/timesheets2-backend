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

@Entity
@Table(name = "Project")
@Getter
@Setter
@EqualsAndHashCode
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    Boolean archived;

    // How many days in the past can a work record be edited or created.
    // 0 if work records can be created or edited for any past date.
    @Column(name = "days_wr_upsertable")
    Integer daysWorkRecordUpsertable;

}
