package com.store.utils.reportes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.store.dto.ProductDTO;
import com.store.entitys.Product;
import com.store.service.ProductsService;

public class SalesExporterPDF {

    @Autowired
    private ProductsService service;

    public ByteArrayOutputStream generatePDF(List<ProductDTO> products, Date start, String type)
            throws DocumentException {

        List<Product> productss = products.stream().map(product -> mapProductEntity(product))
                .collect(Collectors.toList());

        return this.export(productss, start, type);
    }

    private Product mapProductEntity(ProductDTO productDTO) {
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productDTO, Product.class);
        return product;
    }

    private void writeTableWeeklyHeader(PdfPTable table, Date start) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.RED);
        cell.setPadding(9);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        /**
         * Le resto 1 para poder empezar el for en el día correcto
         */
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

    private void writeTableMonthlyHeader(PdfPTable table, Date start) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GREEN);
        /**
         * Son 7 porque son 5 semanas más las columnas Nombre y Total
         */
        cell.setPadding(7);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Nombre", font));
        table.addCell(cell);

        for (int i = 0; i < 5; i++) {
            cell.setPhrase(new Phrase("Semana " + (i + 1), font));
            table.addCell(cell);
        }

        cell.setPhrase(new Phrase("Total", font));
        table.addCell(cell);
    }

    private void writeTableAnnualHeader(PdfPTable table, Date start) {
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

    private void writeTableWeeklyDate(PdfPTable table, List<Product> products, Date date) {

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

    private void writeTableMonthlyDate(PdfPTable table) {

    }

    private void writeTableAnnualDate(PdfPTable table) {

    }

    public ByteArrayOutputStream export(List<Product> products, Date start, String type) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, bos);

            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setColor(Color.GREEN);
            font.setSize(18);

            Paragraph title = new Paragraph(type.toUpperCase() + " REPORT");
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(10);

            document.add(title);
            int columnNum = 8;
            List<Float> widths = new ArrayList<>();
            if (type.equals("weekly")) {
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
            } else if (type.equals("monthly")) {
                columnNum = 7;
                widths.add(4f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
                widths.add(2f);
            } else if (type.equals("annual")) {
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

            if (type.equals("weekly")) {
                writeTableWeeklyHeader(table, start);
                writeTableWeeklyDate(table, products, start);
            } else if (type.equals("monthly")) {
                writeTableMonthlyHeader(table, start);
                writeTableMonthlyDate(table);
            } else if (type.equals("annual")) {
                writeTableAnnualHeader(table, start);
                writeTableAnnualDate(table);
            }

            document.add(table);
            document.close();

            return bos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    

}
