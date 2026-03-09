package com.example.timesheets2.exception;

public class ProjectsRecordsNotUpsertableException extends RuntimeException {
    public ProjectsRecordsNotUpsertableException() {
        super("Records cannot be created or updated for this project");
    }
}
