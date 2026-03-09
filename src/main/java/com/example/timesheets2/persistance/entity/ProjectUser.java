package com.example.timesheets2.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ProjectUser")
@Getter
@Setter
@EqualsAndHashCode
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "project_id")
    Long projectId;

    @Column(name = "user_id")
    String userId;

    @Column(name = "hourly_cost")
    @Convert(converter = MonetaryValueConverter.class)
    BigDecimal hourlyCost;

}
