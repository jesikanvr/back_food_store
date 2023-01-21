package com.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.dto.OrderDTO;
import com.store.dto.OrdersResponse;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class TestOrders {

    @Autowired
    private OrdersService service;

    @Test
    @Order(1)
    public void reciveOrderTest() {

        // Direcciones a la que se enviara el pedido
        Collection<Long> addressess = new ArrayList<>();
        addressess.add(Long.valueOf(2)); // Dirección con id 2 es inválida

        // Par id de producto-cantidad
        Map<Long, Integer> products = new HashMap<>();
        products.put(Long.valueOf(1), 5);
        products.put(Long.valueOf(2), 10);
        products.put(Long.valueOf(3), 5);
        products.put(Long.valueOf(4), 15);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddresses(addressess);
        orderDTO.setProducts(products);

        Boolean response = service.receiveOrder(Long.valueOf(3), orderDTO);
        assertEquals(true, response);

    }

    @Test
    @Order(2)
    public void getMinDateTest() {

        /**
         * Devuelve la fecha de la primera orden recibida
         */
        Date min_date = service.getMinDate();

        assertNotNull(min_date);

    }

    @Test
    @Order(3)
    public void getAllOrdersByRecepcionistTest() {
        OrdersResponse response = service.getAllOrders(Long.valueOf(2), 0,
                5, "id", "asc", null);
        assertNotNull(response.getContent());
        assertEquals(5, response.getContent().size());
    }

    @Test
    @Order(4)
    public void setStatusTest() {
        service.setStatus(Long.valueOf(1), 1); // Estatus 1 es 
    }

}
