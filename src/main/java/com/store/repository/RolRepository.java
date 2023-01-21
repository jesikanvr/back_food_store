package com.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.entitys.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
	public Optional<Rol> findByName(String name);
	public List<Rol> findAll();
}
