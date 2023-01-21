package com.store.service;

import java.util.List;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;
import com.store.dto.ReportDTO;

public interface ReportsService {

    public ReportDTO saveReport(ReportDTO report);

    public List<ReportDTO> getAllReports();

    public void deleteReport(Long reportId);

    public void generatePDF(HttpServletResponse response, Long reportId) throws DocumentException, IOException;

    public void generateExcel(HttpServletResponse response, Long reportId) throws DocumentException, IOException;

}
