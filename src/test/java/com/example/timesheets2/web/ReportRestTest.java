package com.example.timesheets2.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(scripts = {"/import_projects.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(scripts = {"/delete_projects.sql"}, executionPhase = AFTER_TEST_CLASS)
public class ReportRestTest extends RestClientTest {
    @Test
    void shouldNotBeAbleToCreateReportForNonExistentUser() {
        restTestClient.get()
                .uri("http://localhost:%d/v1/reports/1?from=2025-01-01&to=2025-01-20&users=nonexistentuser,".formatted(port))
                .exchange().expectStatus().isBadRequest();
    }
}
