package com.example.timesheets2.controller;

import com.example.timesheets2.domain.WorkRecordUpsert;
import com.example.timesheets2.persistance.entity.WorkRecord;
import com.example.timesheets2.service.WorkRecordService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/v1/work-records")
public class WorkRecordController {

    private final WorkRecordService service;

    WorkRecordController(WorkRecordService service) {
        this.service = service;
    }

    @GetMapping("/month/{month}/{year}")
    List<WorkRecord> getMonthWorkRecords(@PathVariable Integer year, @PathVariable Integer month) {
        return service.getMonthWorkRecords(month, year);
    }

    @PutMapping("/{id}")
    WorkRecord updateWorkRecord(@PathVariable Long id, @RequestBody @Valid WorkRecordUpsert upsert) {
        return service.updateWorkRecord(id, upsert);
    }

    @PostMapping()
    WorkRecord createWorkRecord(@RequestBody @Valid WorkRecordUpsert upsert) {
        return service.createWorkRecord(upsert);
    }

    @DeleteMapping("/{id}")
    void deleteWorkRecord(@PathVariable Long id) {
        service.deleteWorkRecord(id);
    }

}
