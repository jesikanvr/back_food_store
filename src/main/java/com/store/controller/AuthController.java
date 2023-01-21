package com.store.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.LoginDTO;
import com.store.dto.UserDTO;
import com.store.security.JWTAuthResonseDTO;
import com.store.security.JwtTokenProvider;
import com.store.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private UserService serviceUser;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@GetMapping("/current-user")
	public UserDTO getCurrentUser(Principal principal) {
		return serviceUser.findByUser(principal.getName());
	}
	
	@PostMapping("/login")
	public ResponseEntity<JWTAuthResonseDTO> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
		Authentication authentication = manager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Obtener el token del jwtTokenProvider
		String token = jwtTokenProvider.buildToken(authentication);
		return ResponseEntity.ok(new JWTAuthResonseDTO(token));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
		if (serviceUser.existsByUser(userDTO.getUser())) {
			return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
		}
		if (serviceUser.existsByEmail(userDTO.getEmail())) {
			return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
		}

		userDTO.setPass(encoder.encode(userDTO.getPass()));
		serviceUser.createUser(userDTO);

		return new ResponseEntity<>("Successfully registered user", HttpStatus.OK);
	}

}
