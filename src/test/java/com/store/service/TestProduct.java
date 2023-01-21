package com.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.store.dto.ProductsResponse;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class TestProduct {

    @Autowired
    private ProductsService service;

    @Test
    @Order(1)
    public void testSaveProduct() {
        ProductDTO product = new ProductDTO("Pan con lechón", 400, "Delicioso pan con lechón",
                "https://localhost/api/products-img/files/pexels-pixabay-315755.jpg", "ejemplo", true);
        ProductDTO newProduct = service.createProduct(product);
        assertNotNull(newProduct);
    }

    @Test
    public void editProductTest() {
        ProductDTO editingProduct = new ProductDTO();
        editingProduct.setId(Long.valueOf(1));
        editingProduct.setName("Pan con lechón");
        editingProduct.setImgUrl("https://localhost/api/products-img/files/pexels-rajesh-tp-1633578.jpg");
        editingProduct.setOnSale(true);
        editingProduct.setPrice(400);
        editingProduct.setDescription("La mejor carne de cerdo en un delicioso pan de molde");
        editingProduct.setImgName("pan_lechon.jpg");
        ProductDTO editedProduct = service.updateProduct(editingProduct, editingProduct.getId());
        assertNotNull(editedProduct);
    }

    @Test
    public void deleteProduct() {
        service.deleteProduct(Long.valueOf(2));
    }

    @Test
    public void searchProductoTest() {
        List<ProductDTO> products = service.findAll("vdcvdshcv");
        assertEquals(0, products.size());
    }

    @Test
    public void searchProductsTest() {
        String[] ignore = {"id", "price", "imgUrl", "imgName", "ratings", "deleted"};
        ProductsResponse response = service.getAllProducts(new ProductDTO(), ignore, 0, 3, "id", "asc", "maní");
        assertEquals(0, response.getContent().size());
    }

    @Test
    public void getTotalSalesTest() {
        Date start = Date.valueOf("2023-01-02");
        // System.out.println("Aquiiiiiiiii >>>> "+start.toLocalDate().plusDays(-1).getDayOfWeek().name());
        // System.out.println("Aquiiiiiiiii >>>> "+start.toLocalDate().getMonth().maxLength());
        Date end = Date.valueOf("2023-01-04");
        Double totalSales = service.getTotalSales(Long.valueOf(13), start, end);
        System.out.println("AAqiiiiiiiiiiiiiiiiiiiiii        "+totalSales);
        assertNotNull(totalSales);
    }

}
