package com.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.entitys.Municipalities;

public interface MunicipalitiesRepository extends JpaRepository<Municipalities, Long>{
    public Optional<Municipalities> findByName(String name);
}
