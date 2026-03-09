package com.example.timesheets2.service;

import com.example.timesheets2.domain.ProjectUpsert;
import com.example.timesheets2.exception.NotFoundException;
import com.example.timesheets2.exception.ProjectsRecordsNotUpsertableException;
import com.example.timesheets2.persistance.entity.Project;
import com.example.timesheets2.persistance.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final Clock clock;

    ProjectService(ProjectRepository projectRepository, UserService userService, Clock clock) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.clock = clock;
    }

    static void mapUpsertToProject(Project project, ProjectUpsert upsert) {
        project.setName(upsert.name());
        project.setDescription(upsert.description());
        project.setArchived(upsert.archived());
        project.setDaysWorkRecordUpsertable(upsert.daysWorkRecordUpsertable());
    }

    public List<Project> getProjects(Boolean archived) {
        return projectRepository.findByArchived(archived);
    }

    public List<Project> getMyProjects() {
        return projectRepository.getMyActiveProjects(userService.getUserDn());
    }

    public Map<Long, String> getProjectNames(List<Long> ids) {
        var projects = projectRepository.getMyProjects(ids, userService.getUserDn());
        return projects.stream().collect(Collectors.toMap(Project::getId, Project::getName));
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("project", id));
    }

    public Project updateProject(Long id, ProjectUpsert upsert) {
        return projectRepository.findById(id).map(
                project -> {
                    mapUpsertToProject(project, upsert);
                    return projectRepository.save(project);
                }
        ).orElseThrow(() -> new NotFoundException("project", id));
    }

    public Project createProject(ProjectUpsert upsert) {
        var project = new Project();
        mapUpsertToProject(project, upsert);
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public void validateWorkRecordUpsertable(Long projectId, LocalDate date) {
        var project = getProject(projectId);
        if (project.getArchived()) throw new ProjectsRecordsNotUpsertableException();
        if (project.getDaysWorkRecordUpsertable() == 0) return;
        if (project.getDaysWorkRecordUpsertable() < ChronoUnit.DAYS.between(date, LocalDate.now(clock)))
            throw new ProjectsRecordsNotUpsertableException();
    }

}
