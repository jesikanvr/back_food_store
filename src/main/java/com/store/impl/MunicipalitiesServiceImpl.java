package com.store.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.dto.MunicipalitiesDTO;
import com.store.entitys.Municipalities;
import com.store.repository.MunicipalitiesRepository;
import com.store.service.MunicipalitiesService;

@Service
public class MunicipalitiesServiceImpl implements MunicipalitiesService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MunicipalitiesRepository repository;

    @Override
    public List<MunicipalitiesDTO> getAllMunicipalities() {
        return repository.findAll().stream().map(munic -> mapMunicipalitiesDTO(munic)).collect(Collectors.toList());
    }

    @Override
    public void init() {
        String[] municipalities = { "Diez de Octubre", "Arroyo Naranjo", "Boyeros", "Playa", "Habana del Este",
                "San Miguel del Padrón", "Plaza de la Revolución", "Centro Habana", "Marianao",
                "La Lisa", "Cerro",
                "Guanabacoa", "Habana Vieja", "Cotorro", "Regla" };
        for (String municip : municipalities) {
            if (!repository.findByName(municip).isPresent()) {
                Municipalities municipality = new Municipalities();
                municipality.setName(municip);
                repository.save(municipality);
            }
        }
    }

    private MunicipalitiesDTO mapMunicipalitiesDTO(Municipalities municipalities) {
        return mapper.map(municipalities, MunicipalitiesDTO.class);
    }

    private Municipalities mapMunicipalitiesEntity(MunicipalitiesDTO dto) {
        return mapper.map(dto, Municipalities.class);
    }

}
