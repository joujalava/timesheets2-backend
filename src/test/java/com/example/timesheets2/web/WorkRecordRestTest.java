package com.example.timesheets2.web;

import com.example.timesheets2.domain.WorkRecordUpsert;
import com.example.timesheets2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(scripts = {"/import_projects.sql", "/import_work_records.sql", "/import_project_users.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(scripts = {"/delete_work_records.sql", "/delete_project_users.sql", "/delete_projects.sql"}, executionPhase = AFTER_TEST_CLASS)
public class WorkRecordRestTest extends RestClientTest {

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setup() {
        when(userService.getUserDn()).thenReturn("userid");
    }

    @Test
    void shouldNotBeAbleToAddWorkRecordToProjectHeIsNotMemberOf() {
        var workRecord = new WorkRecordUpsert(LocalDate.of(2025, 1, 1), 120, 1L, null);
        restTestClient.post()
                .uri("http://localhost:%d/v1/work-records".formatted(port))
                .contentType(APPLICATION_JSON)
                .body(workRecord)
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    void shouldNotBeAbleToEditSomeoneElseWorkRecord() {
        var workRecord = new WorkRecordUpsert(LocalDate.of(2025, 1, 1), 120, 1L, null);
        restTestClient.put()
                .uri("http://localhost:%d/v1/work-records/1".formatted(port))
                .contentType(APPLICATION_JSON)
                .body(workRecord)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void shouldNotBeAbleToDeleteSomeoneElseWorkRecord() {
        restTestClient.delete()
                .uri("http://localhost:%d/v1/work-records/1".formatted(port))
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void shouldNotBeAbleAddWorkRecordToArchivedProject() {
        var workRecord = new WorkRecordUpsert(LocalDate.of(2025, 1, 1), 120, 2L, null);
        restTestClient.post()
                .uri("http://localhost:%d/v1/work-records".formatted(port))
                .contentType(APPLICATION_JSON)
                .body(workRecord)
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    void shouldNotBeAbleToAddWorkRecordsOlderThanAllowedByProject() {
        var workRecord = new WorkRecordUpsert(LOCAL_DATE.minusDays(3), 120, 2L, null);
        restTestClient.post()
                .uri("http://localhost:%d/v1/work-records".formatted(port))
                .contentType(APPLICATION_JSON)
                .body(workRecord)
                .exchange().expectStatus().isBadRequest();
    }
}
