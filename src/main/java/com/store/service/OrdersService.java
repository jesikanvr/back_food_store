package com.store.service;

import java.sql.Date;

import com.store.dto.OrderDTO;
import com.store.dto.OrdersResponse;

public interface OrdersService {

    public Boolean receiveOrder(Long userId, OrderDTO orderDTO);

    public Date getMinDate();

    public OrdersResponse getAllOrders(Long recepcId, int pageNum, int pageSize, String sortBy, String sortDir,
            String filter);

    public void setStatus(Long id, int status);

    public void init();

}
