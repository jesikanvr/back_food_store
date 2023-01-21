package com.store.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.AddressDTO;
import com.store.dto.UserDTO;
import com.store.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService service;

	@CrossOrigin(origins = "https://localhost:9000")
	@PutMapping("/edit/{id}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO dto, @PathVariable(name = "id") Long id) {
		UserDTO userDTO = service.editUser(dto, id);
		/**Busco un usuario con el email que viene */
		UserDTO dto2 = service.findByEmail(userDTO.getEmail());
		if(dto2 != null){
			if(dto2.getUser() != userDTO.getUser()){
				/**Le vuelvo a poner su email original */
				UserDTO dto3 = service.findByUser(userDTO.getUser());
				userDTO.setEmail(dto3.getEmail());
			}
		}
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping("/{id}/address")
	public ResponseEntity<AddressDTO> setAddress(@Valid @RequestBody AddressDTO addressDTO,
			@PathVariable(name = "id") Long id) {
		AddressDTO dto = service.setAddress(addressDTO, id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/address/{id}")
	public void setIsValid(@PathVariable(name = "id") Long id) {
		service.setIsValid(id);
	}

	@GetMapping("/{userId}/address")
	public List<AddressDTO> getAllAddress(@PathVariable(name = "userId") Long userId) {
		return service.getAllAddress(userId);
	}

	@PutMapping("/{userId}/address/{addressId}")
	public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO,
			@PathVariable(name = "addressId") Long addressId) {
		AddressDTO address = service.updateAddress(addressDTO, addressId);
		return ResponseEntity.ok(address);
	}

	@DeleteMapping("/{userId}/address/{addressId}")
	public ResponseEntity<Boolean> deleteAddress(@PathVariable(name = "addressId") Long addressId) {
		service.deleteAddress(addressId);
		return ResponseEntity.ok(true);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/exist/{email}")
	public ResponseEntity<Boolean> existByEmial(@PathVariable(name = "email") String email) {
		return new ResponseEntity<>(service.existsByEmail(email), HttpStatus.ACCEPTED);
	}

}
