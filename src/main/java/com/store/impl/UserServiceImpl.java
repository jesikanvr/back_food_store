package com.store.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.store.dto.AddressDTO;
import com.store.dto.UserDTO;
import com.store.entitys.Address;
import com.store.entitys.Order;
import com.store.entitys.Rol;
import com.store.entitys.User;
import com.store.exceptions.ResourceNotFoundException;
import com.store.repository.AddressRepository;
import com.store.repository.OrdersRepository;
import com.store.repository.RolRepository;
import com.store.repository.UsersRepository;
import com.store.service.UserService;
import com.store.utils.ConstantsApp;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UsersRepository repo;

	@Autowired
	private AddressRepository addrRepository;

	@Autowired
	private RolRepository repoRol;

	@Autowired
	private OrdersRepository ordersRepository;

	@Override
	public UserDTO createUser(UserDTO userDTO) {

		// Convertimos de DTO a entidad
		User user = mapUserDTO(userDTO);
		Rol rol = repoRol.findByName("ROLE_USER").get();
		user.setRoles(Collections.singleton(rol));

		User newUser = repo.save(user);

		// Convertimos de entidad a DTO
		UserDTO userResponse = mapUserEntity(newUser);

		return userResponse;
	}

	@Override
	public UserDTO editUser(UserDTO userDTO, Long id) {
		User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

		user.setName(userDTO.getName());
		user.setLast_name(userDTO.getLast_name());
		user.setEmail(userDTO.getEmail());
		/**
		 * Atributos opcionales
		 */
		user.setPhone(userDTO.getPhone());

		User updated = repo.save(user);

		return mapUserEntity(updated);
	}

	@Override
	public AddressDTO setAddress(AddressDTO addressDTO, Long id) {
		User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		Address address = mapAddressDTO(addressDTO);
		address.setUser(user);
		try {
			address.setIsValid(existOrderAddress(address));
		} catch (Exception e) {
			// TODO: handle exception
			address.setIsValid(false);
		}
		Address newAddress = addrRepository.save(address);
		return mapAddressEntity(newAddress);
	}

	private boolean existOrderAddress(Address address) {

		JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(ConstantsApp.MAPS_API_KEY);
		JOpenCageForwardRequest request = new JOpenCageForwardRequest(address.getFormatted());
		request.setRestrictToCountryCode("cu"); // restrict results to a specific country

		JOpenCageResponse response = jOpenCageGeocoder.forward(request);
		JOpenCageLatLng firstResultLatLng = response.getFirstPosition(); // get the coordinate pair of the first result

		return firstResultLatLng != null;
	}

	@Override
	public List<AddressDTO> getAllAddress(Long id) {
		User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		List<Address> address = addrRepository.findByUser(user);
		return address.stream().map(add -> mapAddressEntity(add)).collect(Collectors.toList());
	}

	@Override
	public void deleteAddress(Long id) {
		addrRepository.deleteById(id);
	}

	@Override
	public AddressDTO updateAddress(AddressDTO addressDTO, Long id) {
		Address address = addrRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

		address.setAlias(addressDTO.getAlias());
		address.setApto(addressDTO.getApto());
		address.setFormatted(addressDTO.getFormatted());

		Address newAddress = addrRepository.save(address);

		return mapAddressEntity(newAddress);
	}

	@Override
	public UserDTO findByUser(String name) {
		User user = repo.findByUser(name).orElseThrow(() -> new ResourceNotFoundException("User", "name", null));
		return mapUserEntity(user);
	}

	@Override
	public UserDTO findByEmail(String email) {
		User user = repo.findByEmail(email);
		return mapUserEntity(user);
	}

	@Override
	public Boolean existsByUser(String name) {
		return repo.existsByUser(name);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return repo.existsByEmail(email);
	}

	@Override
	public UserDTO findByTokenPassword(String token) {
		User user = repo.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("User", "name", null));
		return mapUserEntity(user);
	}

	@Override
	public void setIsValid(Long addressId) {
		Address address = addrRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		address.setIsValid(true);
		addrRepository.save(address);
		/**
		 * Si se confirmo que la dirección del pedido es válida
		 * entonces el pedido en sí debe cambiar su estado
		 */
		List<Order> orders = ordersRepository.findByAddress(address);
		orders.forEach(order -> {
			order.setState(1);
			ordersRepository.save(order);
		});
	}

	private User mapUserDTO(UserDTO usersDTO) {
		User user = mapper.map(usersDTO, User.class);
		return user;
	}

	private UserDTO mapUserEntity(User user) {
		UserDTO usersDTO = mapper.map(user, UserDTO.class);
		return usersDTO;
	}

	private Address mapAddressDTO(AddressDTO addressDTO) {
		Address address = mapper.map(addressDTO, Address.class);
		return address;
	}

	private AddressDTO mapAddressEntity(Address address) {
		AddressDTO addressDTO = mapper.map(address, AddressDTO.class);
		return addressDTO;
	}

}
