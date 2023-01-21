package com.store.service;

import java.util.List;

import com.store.dto.AddressDTO;
import com.store.dto.UserDTO;

public interface UserService {
	public UserDTO createUser(UserDTO userDTO);

	public UserDTO editUser(UserDTO userDTO, Long id);

	public AddressDTO setAddress(AddressDTO addressDTO, Long id);

	public List<AddressDTO> getAllAddress(Long id);
	
	public void deleteAddress(Long id);
	
	public AddressDTO updateAddress(AddressDTO addressDTO, Long id);

	public void setIsValid(Long addressId);
	
	public UserDTO findByUser(String name);

	public UserDTO findByEmail(String name);
	
	public Boolean existsByUser(String name);
	
	public Boolean existsByEmail(String email);

	public UserDTO findByTokenPassword(String token);
}
