package com.example.timesheets2.service;

import com.example.timesheets2.domain.User;
import com.example.timesheets2.exception.MisformedMonetaryValueException;
import com.example.timesheets2.exception.UserNotFoundException;
import com.example.timesheets2.persistance.entity.ProjectUser;
import com.example.timesheets2.persistance.repository.ProjectUserRepository;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectUserService {
    private final static int MONETARY_VALUE_SCALE = 2;

    private final ProjectUserRepository projectUserRepository;
    private final UserService userService;

    ProjectUserService(ProjectUserRepository projectUserRepository, UserService userService) {
        this.projectUserRepository = projectUserRepository;
        this.userService = userService;
    }

    public List<ProjectUser> getProjectUsers(Long projectId) {
        return projectUserRepository.getByProjectId(projectId);
    }

    public void setProjectUsers(Long projectId, Map<String, BigDecimal> upsertMap) throws NamingException {
        var uids = userService.getUsers().stream().map(User::id).collect(Collectors.toSet());
        if (!uids.containsAll(upsertMap.keySet())) {
            var missingUsers = new ArrayList<>(upsertMap.keySet());
            missingUsers.removeAll(uids);
            throw new UserNotFoundException(missingUsers);
        }
        var projectUsers = projectUserRepository.getByProjectId(projectId);
        for (ProjectUser projectUser : projectUsers) {
            if (upsertMap.containsKey(projectUser.getUserId())) {
                var newHourlyCost = upsertMap.get(projectUser.getUserId());
                validateMonetaryValue(newHourlyCost);
                projectUser.setHourlyCost(newHourlyCost);
                projectUserRepository.save(projectUser);
            } else {
                projectUserRepository.delete(projectUser);
            }
        }
        var userIds = projectUsers.stream().map(ProjectUser::getUserId).collect(Collectors.toSet());
        for (var entry : upsertMap.entrySet()) {
            if (userIds.contains(entry.getKey())) continue;
            var projectUser = new ProjectUser();
            projectUser.setProjectId(projectId);
            projectUser.setUserId(entry.getKey());
            validateMonetaryValue(entry.getValue());
            projectUser.setHourlyCost(entry.getValue());
            projectUserRepository.save(projectUser);
        }
    }

    public void deleteProjectUsers(Long projectId) {
        projectUserRepository.deleteByProjectId(projectId);
    }

    public void validateUserInProject(Long projectId, String userId) {
        if (!projectUserRepository.existsByProjectIdAndUserId(projectId, userId))
            throw new UserNotFoundException(List.of(userId));
    }

    private void validateMonetaryValue(BigDecimal monetaryValue) {
        if (monetaryValue.scale() > MONETARY_VALUE_SCALE) throw new MisformedMonetaryValueException();
    }

}
