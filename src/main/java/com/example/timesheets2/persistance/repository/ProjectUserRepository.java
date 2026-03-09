package com.example.timesheets2.persistance.repository;

import com.example.timesheets2.persistance.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    @Transactional
    @Modifying
    @Query("delete from ProjectUser pu where pu.projectId = :projectId")
    void deleteByProjectId(Long projectId);

    List<ProjectUser> getByProjectId(Long projectId);

    Boolean existsByProjectIdAndUserId(Long projectId, String userId);
}
