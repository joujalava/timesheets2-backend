package com.example.timesheets2.controller;

import com.example.timesheets2.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/v1/reports")
public class ReportController {
    static final String CONTENT_TYPE_PDF = "application/pdf";

    private final ReportService service;

    ReportController(ReportService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    void getReport(@PathVariable Long id, @RequestParam LocalDate from, @RequestParam LocalDate to, @RequestParam List<String> users, HttpServletResponse response) throws NamingException, IOException {
        response.setContentType(CONTENT_TYPE_PDF);
        service.getReport(from, to, id, users, response.getOutputStream());
    }

}
