package com.example.timesheets2.controller;

import com.example.timesheets2.domain.ProjectUpsert;
import com.example.timesheets2.persistance.entity.Project;
import com.example.timesheets2.persistance.entity.ProjectUser;
import com.example.timesheets2.service.ProjectService;
import com.example.timesheets2.service.ProjectUserService;
import com.example.timesheets2.service.WorkRecordService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController()
@RequestMapping("/v1/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectUserService projectUserService;
    private final WorkRecordService workRecordService;

    ProjectController(ProjectService projectService, ProjectUserService projectUserService, WorkRecordService workRecordService) {
        this.projectService = projectService;
        this.projectUserService = projectUserService;
        this.workRecordService = workRecordService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    List<Project> getProjects(@RequestParam(defaultValue = "false", required = false) Boolean archived) {
        return projectService.getProjects(archived);
    }

    @GetMapping("/me")
    List<Project> getMyProjects() {
        return projectService.getMyProjects();
    }

    @GetMapping("/names")
    Map<Long, String> getProjectsNames(@RequestParam List<Long> ids) {
        return projectService.getProjectNames(ids);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    Project getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    Project updateProject(@PathVariable Long id, @RequestBody @Valid ProjectUpsert upsert) {
        return projectService.updateProject(id, upsert);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    Project createProject(@RequestBody @Valid ProjectUpsert upsert) {
        return projectService.createProject(upsert);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    void deleteProject(@PathVariable Long id) {
        workRecordService.deleteProjectsWorkRecords(id);
        projectUserService.deleteProjectUsers(id);
        projectService.deleteProject(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{projectId}/users")
    List<ProjectUser> getProjectUsers(@PathVariable Long projectId) {
        return projectUserService.getProjectUsers(projectId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{projectId}/users")
    void setProjectUsers(@PathVariable Long projectId, @RequestBody Map<String, BigDecimal> upsertMap) throws NamingException {
        projectUserService.setProjectUsers(projectId, upsertMap);
    }

}
