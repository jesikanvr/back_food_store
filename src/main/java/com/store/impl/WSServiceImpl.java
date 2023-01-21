package com.store.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.store.dto.OrderDTO;
import com.store.service.WSService;

@Service
public class WSServiceImpl implements WSService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyFrontend(OrderDTO order) {
        messagingTemplate.convertAndSend("/topic/messages", order);
    }

}
