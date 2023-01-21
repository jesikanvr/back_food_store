package com.store.service;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.store.repository.ProductsRepository;

import com.store.utils.customs.SalesProduct;

@DataJpaTest
public class JpaTest {

    @Autowired
    private ProductsRepository repository;

    @Test
    public void getTotalSales() {
        double d = repository.getNetPrice(Long.valueOf(13), Date.valueOf("2022-11-15"), Date.valueOf("2022-12-25"));
    }

    @Test
    public void getAllTotalSales() {
        Date start = Date.valueOf("2022-12-22");
        Date end = Date.valueOf("2022-12-29");
        Long[] ids = { (long) 1, (long) 3, (long) 7, (long) 5 };
        // List<SalesProduct> data = repository.getSaleNetPrice(ids, start, end);
    }

}
