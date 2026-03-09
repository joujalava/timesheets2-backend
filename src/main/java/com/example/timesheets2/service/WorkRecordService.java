package com.example.timesheets2.service;

import com.example.timesheets2.domain.WorkRecordUpsert;
import com.example.timesheets2.exception.NotFoundException;
import com.example.timesheets2.persistance.entity.WorkRecord;
import com.example.timesheets2.persistance.repository.WorkRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class WorkRecordService {

    private final WorkRecordRepository repository;

    private final UserService userService;
    private final ProjectUserService projectUserService;
    private final ProjectService projectService;

    WorkRecordService(WorkRecordRepository repository, UserService userService, ProjectService projectService, ProjectUserService projectUserService) {
        this.repository = repository;
        this.userService = userService;
        this.projectService = projectService;
        this.projectUserService = projectUserService;
    }

    static void mapUpsertToWorkRecord(WorkRecord workRecord, WorkRecordUpsert upsert) {
        workRecord.setDate(upsert.date());
        workRecord.setMinutes(upsert.minutes());
        workRecord.setProjectId(upsert.projectId());
        workRecord.setDescription(upsert.description());
    }

    public List<WorkRecord> getMonthWorkRecords(Integer month, Integer year) {
        return repository.findByDateInMonth(month, year, userService.getUserDn());
    }

    public List<WorkRecord> getWorkRecordsBetweenDate(LocalDate start, LocalDate end, Long projectId, List<String> userIds) {
        return repository.findBetweenDatesByProjectAndUsers(start, end, projectId, userIds);
    }

    public WorkRecord updateWorkRecord(Long id, WorkRecordUpsert upsert) {
        var userId = userService.getUserDn();
        return repository.findById(id).filter(workRecord -> Objects.equals(workRecord.getUserId(), userId)).map(
                workRecord -> {
                    validateWorkRecord(upsert, userId);
                    mapUpsertToWorkRecord(workRecord, upsert);
                    return repository.save(workRecord);
                }
        ).orElseThrow(() -> new NotFoundException("work record", id));
    }

    public WorkRecord createWorkRecord(WorkRecordUpsert upsert) {
        var userId = userService.getUserDn();
        validateWorkRecord(upsert, userId);
        var workRecord = new WorkRecord();
        mapUpsertToWorkRecord(workRecord, upsert);
        workRecord.setUserId(userId);
        return repository.save(workRecord);
    }

    public void deleteWorkRecord(Long id) {
        repository.delete(
                repository.findById(id)
                        .filter(workRecord -> Objects.equals(workRecord.getUserId(), userService.getUserDn()))
                        .orElseThrow(() -> new NotFoundException("work record", id)));
    }

    public void deleteProjectsWorkRecords(Long projectId) {
        repository.deleteByProjectId(projectId);
    }

    void validateWorkRecord(WorkRecordUpsert upsert, String userId) {
        projectUserService.validateUserInProject(upsert.projectId(), userId);
        projectService.validateWorkRecordUpsertable(upsert.projectId(), upsert.date());
    }
}
