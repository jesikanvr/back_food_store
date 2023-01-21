package com.store.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.OrderDTO;
import com.store.dto.OrdersResponse;
import com.store.service.OrdersService;
import com.store.utils.ConstantsApp;

@RestController
@RequestMapping("/api/my-orders")
public class OrdersController {

    @Autowired
    private OrdersService service;

    @GetMapping("/{recepId}")
    public OrdersResponse getAllOrders(
            @PathVariable(name = "recepId") Long recepId,
            @RequestParam(value = "pageNum", defaultValue = ConstantsApp.PAGE_NUM_DEFAULT, required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = ConstantsApp.PAGE_SIZE_DEFAUT, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ConstantsApp.PAGE_ORDER_BY_DEFAULT, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ConstantsApp.PAGE_ORDER_DIR_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "filter", required = false) String filter) {
        return service.getAllOrders(recepId, pageNum, pageSize, sortBy, sortDir, filter);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Boolean> receiveOrder(@PathVariable(name = "userId") Long userId,
            @RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(service.receiveOrder(userId, orderDTO), HttpStatus.CREATED);
    }

    @GetMapping("/min-date")
    public ResponseEntity<Date> getMinDateSale() {
        return ResponseEntity.ok(service.getMinDate());
    }

    @PostMapping("/{id}/{state}")
    public void updateState(@PathVariable(name = "id") Long id, @PathVariable(name = "state") int state) {
        service.setStatus(id, state);
    }

}
