package com.example.timesheets2.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectUpsert(@NotNull @NotBlank(message = "Project has to have a name") String name, String description,
                            @NotNull Boolean archived, @NotNull @Min(0) Integer daysWorkRecordUpsertable) {
}
