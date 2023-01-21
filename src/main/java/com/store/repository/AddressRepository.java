package com.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.entitys.Address;
import com.store.entitys.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
	public List<Address> findByUser(User user);
}
