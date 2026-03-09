package com.example.timesheets2.persistance.repository;

import com.example.timesheets2.persistance.entity.WorkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface WorkRecordRepository extends JpaRepository<WorkRecord, Long> {
    @Query("from WorkRecord r where month(r.date) = :month and year(r.date) = :year and r.userId = :userId")
    List<WorkRecord> findByDateInMonth(Integer month, Integer year, String userId);

    @Query("from WorkRecord r where r.date >= :start and r.date <= :end and r.projectId = :projectId and r.userId in (:userIds)")
    List<WorkRecord> findBetweenDatesByProjectAndUsers(LocalDate start, LocalDate end, Long projectId, List<String> userIds);

    @Transactional
    @Modifying
    @Query("delete from WorkRecord r where r.projectId = :projectId")
    void deleteByProjectId(Long projectId);
}
