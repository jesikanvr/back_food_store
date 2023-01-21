package com.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.MunicipalitiesDTO;
import com.store.service.MunicipalitiesService;

@RestController
@RequestMapping("/api/municipalities")
public class MunicipalitiesController {

    @Autowired
    private MunicipalitiesService service;

    @GetMapping
    public List<MunicipalitiesDTO> getAllMunicipalities() {
        return service.getAllMunicipalities();
    }

}
