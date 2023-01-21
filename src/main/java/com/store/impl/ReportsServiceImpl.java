package com.store.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.store.dto.ProductDTO;
import com.store.dto.ReportDTO;
import com.store.entitys.Product;
import com.store.entitys.Report;
import com.store.exceptions.ResourceNotFoundException;
import com.store.repository.ReportsRepository;
import com.store.service.ProductsService;
import com.store.service.ReportsService;
import java.awt.Color;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private ReportsRepository repository;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductsService service;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Override
    public ReportDTO saveReport(ReportDTO reportDTO) {
        reportDTO.setStartDate(getStartDateByType(reportDTO.getEndDate(), reportDTO.getType()));
        if (reportDTO.getProducts() == null || reportDTO.getProducts().isEmpty()) {
            Collection<ProductDTO> products = productsService.getNotDeletedProducts();
            reportDTO.setProducts(products);
        }
        Report newReport = mapReportEntity(reportDTO);
        Report savReport = repository.save(newReport);
        return mapReportDTO(savReport);
    }

    @Override
    public List<ReportDTO> getAllReports() {
        List<Report> reports = repository.findAll();
        reports.forEach(report -> {
            report.setProducts(null);
        });
        return reports.stream().map(report -> mapReportDTO(report)).collect(Collectors.toList());
    }

    @Override
    public void deleteReport(Long reportId) {
        repository.deleteById(reportId);
    }

    @Override
    public void generatePDF(HttpServletResponse response, Long reportId)
            throws DocumentException, IOException {
        Report report = repository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        Date date = getStartDateByType(report.getEndDate(), report.getType());
        report.setStartDate(date);
        List<Product> products = new ArrayList<>(report.getProducts());
        byte[] pdfReport = this.exportPDF(products, date, report.getType()).toByteArray();
        response.setContentType("apllication/pdf");
        String header = "Content-Disposition";
        String valor = "attachment; filename=" + report.getName() + ".pdf";
        response.setHeader(header, valor);
        response.setContentLength(pdfReport.length);

        ByteArrayInputStream inStream = new ByteArrayInputStream(pdfReport);
        FileCopyUtils.copy(inStream, response.getOutputStream());

    }

    @Override
    public void generateExcel(HttpServletResponse response, Long reportId) throws DocumentException, IOException {
        Report report = repository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        Date date = getStartDateByType(report.getEndDate(), report.getType());
        report.setStartDate(date);
        List<Product> products = new ArrayList<>(report.getProducts());
        byte[] excelReport = this.exportExcel(products, date, report.getType()).toByteArray();
        response.setContentType("apllication/octec-stream");
        String header = "Content-Disposition";
        String valor = "attachment; filename=" + report.getName() + ".xlsx";
        response.setHeader(header, valor);
        response.setContentLength(excelReport.length);

        ByteArrayInputStream inStream = new ByteArrayInputStream(excelReport);
        FileCopyUtils.copy(inStream, response.getOutputStream());
    }

    public ByteArrayOutputStream exportPDF(List<Product> products, Date start, int type) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, bos);

            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setColor(Color.GREEN);
            font.setSize(18);

            int columnNum = 8;
            List<Float> widths = new ArrayList<>();
            if (type == 1) {
                Paragraph title = new Paragraph("WEEKLY REPORT");
                title.setAlignment(Paragraph.ALIGN_CENTER);
                title.setSpacingAfter(10);
                document.add(title);

                columnNum = 9;
                widths.add(3.5f);
                widths.add(1f);
                widths.add(1f);
                widths.add(1f);
                widths.add(1f);
                widths.add(1f);
                widths.add(1f);
                widths.add(1f);
                widths.add(1f);
            } else if (type == 2) {
                Paragraph title = new Paragraph("MONTHLY REPORT");
                title.setAlignment(Paragraph.ALIGN_CENTER);
                title.setSpacingAfter(10);
                document.add(title);

                columnNum = 7;
                widths.add(4f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
            } else if (type == 3) {
                Paragraph title = new Paragraph("ANNUAL REPORT");
                title.setAlignment(Paragraph.ALIGN_CENTER);
                title.setSpacingAfter(10);
                document.add(title);

                columnNum = 14;
                widths.add(4f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
            }

            PdfPTable table = new PdfPTable(columnNum);
            table.setWidthPercentage(100);
            table.spacingBefore();
            float[] fs = new float[widths.size()];
            for (int i = 0; i < widths.size(); i++) {
                fs[i] = widths.get(i);
            }
            table.setWidths(fs);
            table.setWidthPercentage(110);

            if (type == 1) {
                writeTableWeeklyHeaderPDF(table, start);
                writeTableWeeklyDatePDF(table, products, start);
            } else if (type == 2) {
                writeTableMonthlyHeaderPDF(table, start);
                writeTableMonthlyDatePDF(table, products, start);
            } else if (type == 3) {
                writeTableAnnualHeaderPDF(table, start);
                writeTableAnnualDatePDF(table);
            }

            document.add(table);
            document.close();

            return bos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ByteArrayOutputStream exportExcel(List<Product> products, Date start, int type) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook = new XSSFWorkbook();
            if (type == 1) {
                sheet = workbook.createSheet("Weekly report");
                writeTableWeeklyHeaderExcel(start);
                writeTableWeeklyDateExcel(products, start);
            } else if (type == 2) {
                sheet = workbook.createSheet("Monthly report");
                writeTableMontlyHeaderExcel(start);
                writeTableMontlyDateExcel(products, start);
            } else {
                sheet = workbook.createSheet("Anual report");
            }

            workbook.write(bos);
            workbook.close();
            return bos;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Report mapReportEntity(ReportDTO reportDTO) {
        Report report = mapper.map(reportDTO, Report.class);
        return report;
    }

    private ReportDTO mapReportDTO(Report report) {
        ReportDTO reportDTO = mapper.map(report, ReportDTO.class);
        return reportDTO;
    }

    private Date getStartDateByType(Date endDate, int type) {
        Date date = null;
        switch (type) {
            case 1:
                date = Date.valueOf(endDate.toLocalDate().plusDays(-6));
                return date;
            case 2:
                date = Date.valueOf(endDate.toLocalDate().plusDays(-30));
                return date;
            case 3:
                date = Date.valueOf(endDate.toLocalDate().plusDays(-364));
                return date;
            default:
                return endDate;
        }
    }

    private void writeTableWeeklyHeaderPDF(PdfPTable table, Date start) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.RED);
        cell.setPadding(9);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        DayOfWeek dayOfWeek = start.toLocalDate().getDayOfWeek();

        cell.setPhrase(new Phrase("Product name", font));
        table.addCell(cell);

        for (int i = 0; i < 7; i++) {
            cell.setPhrase(new Phrase(dayOfWeek.plus(i).name().substring(0, 1), font));
            table.addCell(cell);
        }

        cell.setPhrase(new Phrase("Total", font));
        table.addCell(cell);
    }

    private void writeTableMonthlyHeaderPDF(PdfPTable table, Date start) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.RED);
        /**
         * Son 7 porque son 5 semanas más las columnas Nombre y Total
         */
        cell.setPadding(7);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Product name", font));
        table.addCell(cell);

        for (int i = 0; i < 5; i++) {
            cell.setPhrase(new Phrase("Semana " + (i + 1), font));
            table.addCell(cell);
        }

        cell.setPhrase(new Phrase("Total", font));
        table.addCell(cell);
    }

    private void writeTableAnnualHeaderPDF(PdfPTable table, Date start) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GREEN);
        cell.setPadding(14);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        Month month = start.toLocalDate().getMonth();

        cell.setPhrase(new Phrase("Product name", font));
        table.addCell(cell);

        for (int i = 0; i < 12; i++) {
            cell.setPhrase(new Phrase(month.plus(i).name(), font));
            table.addCell(cell);
        }

        cell.setPhrase(new Phrase("Total", font));
        table.addCell(cell);
    }

    private void writeTableWeeklyDatePDF(PdfPTable table, List<Product> products, Date date) {

        LocalDate localDate = date.toLocalDate();
        products.forEach(product -> {
            table.addCell(product.getName());
            double total = 0;
            for (int i = 0; i < 7; i++) {
                Date day = Date.valueOf(localDate.plusDays(i));
                Object value = service.getTotalSales(product.getId(), day, day);
                double totalSales = value != null ? (Double) value : 0;

                total += totalSales;
                table.addCell(String.valueOf(totalSales));

            }
            table.addCell(String.valueOf(total));
        });

    }

    private void writeTableMonthlyDatePDF(PdfPTable table, List<Product> products, Date date) {

        LocalDate localDate = date.toLocalDate();
        products.forEach(product -> {
            table.addCell(product.getName());
            double total = 0;
            for (int i = 0; i < 28; i += 7) {
                Date startDay = Date.valueOf(localDate.plusDays(i));
                Date endDate = Date.valueOf(startDay.toLocalDate().plusDays(7));
                Object value = service.getTotalSales(product.getId(), startDay, endDate);
                double totalSales = value != null ? (Double) value : 0;

                total += totalSales;
                table.addCell(String.valueOf(totalSales));

            }
            /**
             * Esto es para los dos dias restantes de los 30 dias de un mes
             * ya que el for anterior se queda en 28 dias
             */
            Date startDay = Date.valueOf(localDate.plusDays(28));
            Date endDate = Date.valueOf(localDate.plusDays(30));
            Object value = service.getTotalSales(product.getId(), startDay, endDate);
            double totalSales = value != null ? (Double) value : 0;
            table.addCell(String.valueOf(totalSales));

            table.addCell(String.valueOf(total));
        });

    }

    private void writeTableAnnualDatePDF(PdfPTable table) {

    }

    private void writeTableWeeklyHeaderExcel(Date start) {
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        Cell cell = row.createCell(0);
        cell.setCellValue("Product name");
        cell.setCellStyle(style);

        DayOfWeek dayOfWeek = start.toLocalDate().getDayOfWeek();

        for (int i = 1; i < 8; i++) {
            cell = row.createCell(i);
            cell.setCellValue(dayOfWeek.plus(i - 1).name().substring(0, 1));
            cell.setCellStyle(style);
        }

        cell = row.createCell(8);
        cell.setCellValue("Total");
        cell.setCellStyle(style);
    }

    private void writeTableMontlyHeaderExcel(Date start) {
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        Cell cell = row.createCell(0);
        cell.setCellValue("Product name");
        cell.setCellStyle(style);

        for (int i = 1; i < 6; i++) {
            cell = row.createCell(i);
            cell.setCellValue("Semana " + i);
            cell.setCellStyle(style);
        }

        cell = row.createCell(6);
        cell.setCellValue("Total");
        cell.setCellStyle(style);
    }

    private void writeTableWeeklyDateExcel(List<Product> products, Date date) {
        int numRows = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        LocalDate localDate = date.toLocalDate();
        for (Product product : products) {
            Row row = sheet.createRow(numRows++);
            double total = 0;
            Cell cell = null;
            cell = row.createCell(0);
            cell.setCellValue(product.getName());
            sheet.autoSizeColumn(0);
            cell.setCellStyle(style);
            for (int i = 1; i < 8; i++) {
                cell = row.createCell(i);
                Date day = Date.valueOf(localDate.plusDays(i - 1));
                Object value = service.getTotalSales(product.getId(), day, day);
                double totalSales = value != null ? (Double) value : 0;

                total += totalSales;
                cell.setCellValue(totalSales);
                sheet.autoSizeColumn(i + 1);
                cell.setCellStyle(style);
            }
            cell = row.createCell(8);
            cell.setCellValue(total);
            sheet.autoSizeColumn(8);
            cell.setCellStyle(style);
        }

    }

    private void writeTableMontlyDateExcel(List<Product> products, Date date) {

        int numRows = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        LocalDate localDate = date.toLocalDate();
        for (Product product : products) {
            Row row = sheet.createRow(numRows++);
            double total = 0;
            Cell cell = null;
            cell = row.createCell(0);
            cell.setCellValue(product.getName());
            sheet.autoSizeColumn(0);
            cell.setCellStyle(style);
            int cellIndex = 1;
            for (int i = 1; i < 29; i += 7) {
                cell = row.createCell(cellIndex);
                Date startDay = Date.valueOf(localDate.plusDays(i - 1));
                Date endDate = Date.valueOf(startDay.toLocalDate().plusDays(7));
                Object value = service.getTotalSales(product.getId(), startDay, endDate);
                double totalSales = value != null ? (Double) value : 0;

                total += totalSales;
                cell.setCellValue(totalSales);
                sheet.autoSizeColumn(cellIndex);
                cell.setCellStyle(style);

                cellIndex++;
            }

            Date startDay = Date.valueOf(localDate.plusDays(28));
            Date endDate = Date.valueOf(localDate.plusDays(30));
            Object value = service.getTotalSales(product.getId(), startDay, endDate);
            double totalSales = value != null ? (Double) value : 0;
            cell = row.createCell(cellIndex);
            cell.setCellValue(totalSales);
            sheet.autoSizeColumn(cellIndex);
            cell.setCellStyle(style);

            cellIndex++;

            cell = row.createCell(cellIndex++);
            cell.setCellValue(total);
            sheet.autoSizeColumn(cellIndex);
            cell.setCellStyle(style);

            cellIndex++;
        }

    }

}
