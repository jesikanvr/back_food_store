package com.store.service;

import com.store.dto.OrderDTO;

public interface WSService {

    public void notifyFrontend(final OrderDTO order);

}
