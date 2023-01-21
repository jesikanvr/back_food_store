package com.store.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.dto.RoleDTO;
import com.store.entitys.Rol;
import com.store.exceptions.ResourceNotFoundException;
import com.store.repository.RolRepository;
import com.store.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RolRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<RoleDTO> findAll() {
        return repository.findAll().stream().map(user -> mapRoleDTO(user)).collect(Collectors.toList());
    }

    @Override
    public RoleDTO findByName(String name) {
        Rol rol = repository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product", "id", null));
        return mapRoleDTO(rol);
    }

    private Rol mapRolEntity(RoleDTO dto) {
        return mapper.map(dto, Rol.class);
    }

    private RoleDTO mapRoleDTO(Rol entity) {
        return mapper.map(entity, RoleDTO.class);
    }

    @Override
    public void init() {
        Rol[] roles = { new Rol("ROLE_ADMIN"), new Rol("ROLE_USER"), new Rol("ROLE_RECEP") };

        for (Rol rol : roles) {
            if (!repository.findByName(rol.getName()).isPresent()) {
                repository.save(rol);
            }
        }
    }

}
