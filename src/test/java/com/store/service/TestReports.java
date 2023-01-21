package com.store.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.dto.ProductDTO;
import com.store.dto.ReportDTO;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class TestReports {

    @Autowired
    private ReportsService service;

    @Autowired
    private ProductsService productsService;

    @Test
    @Order(1)
    public void saveReportTest() {

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setName("Reporte semanal");
        reportDTO.setType(1); // Tipo semanal
        reportDTO.setEndDate(Date.valueOf(LocalDate.now()));

        Collection<ProductDTO> products = productsService.getNotDeletedProducts();
        reportDTO.setProducts(products);

        service.saveReport(reportDTO);

    }

    @Test
    @Order(2)
    public void getAllReportsTest() {
        List<ReportDTO> reports = service.getAllReports();
        assertNotNull(reports);
    }

    @Test
    @Order(3)
    public void deleteReportTest() {
        service.deleteReport(Long.valueOf(7));
    }

}
