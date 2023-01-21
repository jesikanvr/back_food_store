package com.store.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.dto.OrderDTO;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TestWSService {

    @Autowired
    private WSService service;

    @Test
    public void notifyFrontendTest() {

        OrderDTO orderDTO = new OrderDTO();

        service.notifyFrontend(orderDTO);

    }

}
