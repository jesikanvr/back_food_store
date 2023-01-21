package com.store.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.ReportDTO;
import com.store.service.ReportsService;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private ReportsService service;

    @PostMapping("/create")
    public ResponseEntity<ReportDTO> saveReport(@RequestBody ReportDTO reportDTO) {
        ReportDTO dto = service.saveReport(reportDTO);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reportDTOs = service.getAllReports();
        return ResponseEntity.ok(reportDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReport(@PathVariable(name = "id") Long id) {
        service.deleteReport(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{id}/export-pdf")
    public void exportPDF(HttpServletResponse response, @PathVariable(name = "id") Long reportId) throws IOException {
        service.generatePDF(response, reportId);
    }

    @GetMapping("/{id}/export-excel")
    public void exportExcel(HttpServletResponse response, @PathVariable(name = "id") Long reportId) throws IOException {
        service.generateExcel(response, reportId);
    }

}
