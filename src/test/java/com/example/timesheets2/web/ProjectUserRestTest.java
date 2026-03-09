package com.example.timesheets2.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(scripts = {"/import_projects.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(scripts = {"/delete_projects.sql"}, executionPhase = AFTER_TEST_CLASS)
public class ProjectUserRestTest extends RestClientTest {

    @Test
    void shouldNotAddNonExistentUsersToProject() {
        var usersToAdd = new HashMap<String, BigDecimal>();
        usersToAdd.put("non existent user", new BigDecimal(2));
        restTestClient.post()
                .uri("http://localhost:%d/v1/projects/1/users".formatted(port))
                .contentType(APPLICATION_JSON)
                .body(usersToAdd)
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    void shouldNotAllowSendingMisformedMonetaryValue() {
        var usersToAdd = new HashMap<String, String>();
        usersToAdd.put("non existent user", "2.001");
        restTestClient.post()
                .uri("http://localhost:%d/v1/projects/1/users".formatted(port))
                .contentType(APPLICATION_JSON)
                .body(usersToAdd)
                .exchange().expectStatus().isBadRequest();
    }

}
