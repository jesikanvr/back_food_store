package com.store.service;

import java.util.List;

import com.store.dto.MunicipalitiesDTO;

public interface MunicipalitiesService {

    public void init();
    
    public List<MunicipalitiesDTO> getAllMunicipalities();
    
}
