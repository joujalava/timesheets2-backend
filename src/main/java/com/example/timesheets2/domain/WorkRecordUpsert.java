package com.example.timesheets2.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WorkRecordUpsert(@NotNull LocalDate date, @NotNull @Min(1) @Max(24 * 60) Integer minutes,
                               @NotNull Long projectId,
                               String description) {
}
