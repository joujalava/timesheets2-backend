package com.example.timesheets2.persistance.repository;

import com.example.timesheets2.persistance.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByArchived(Boolean archived);

    @Query("select p from Project p inner join ProjectUser pu on pu.projectId = p.id where pu.userId = :userId and p.archived = FALSE")
    List<Project> getMyActiveProjects(String userId);

    @Query("select p from Project p inner join ProjectUser pu on pu.projectId = p.id where pu.userId = :userId and p.id in (:ids)")
    List<Project> getMyProjects(List<Long> ids, String userId);
}
