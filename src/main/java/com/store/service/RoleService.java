package com.store.service;

import java.util.List;

import com.store.dto.RoleDTO;

public interface RoleService {
    public List<RoleDTO> findAll();       
    
    public RoleDTO findByName(String name);

    public void init();
}
