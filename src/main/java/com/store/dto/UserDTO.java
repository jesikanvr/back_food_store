package com.store.dto;

import java.util.Collection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserDTO {

	private Long id;
	@NotBlank
	@NotEmpty(message = "The name must not be empty")
	@Size(min = 2, message = "Name must contain more than two characters")
	private String name;
	@NotBlank
	@NotEmpty(message = "The last name must not be empty")
	@Size(min = 5, message = "Last name must contain more than five characters")
	private String last_name;
	@NotBlank
	@Size(min = 2, message = "User name must contain more than two characters")
	private String user;
	@NotBlank
	@NotEmpty(message = "The last name must not be empty")
	@Email
	private String email;
	@NotBlank
	@Size(min = 8, message = "Password must contain more than eight characters")
	private String pass;

	private String token_pass;

	private String phone;

	private Collection<RoleDTO> roles;

	private MunicipalitiesDTO municipalities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Collection<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleDTO> roles) {
		this.roles = roles;
	}

	public String getToken_pass() {
		return token_pass;
	}

	public void setToken_pass(String token_pass) {
		this.token_pass = token_pass;
	}

	public MunicipalitiesDTO getMunicipalities() {
		return municipalities;
	}

	public void setMunicipalities(MunicipalitiesDTO municipalities) {
		this.municipalities = municipalities;
	}

	public UserDTO(
			@NotEmpty(message = "The name must not be empty") @Size(min = 2, message = "Name must contain more than two characters") String name,
			@NotEmpty(message = "The last name must not be empty") @Size(min = 5, message = "Last name must contain more than five characters") String last_name,
			@Size(min = 2, message = "User name must contain more than two characters") String user,
			@NotEmpty(message = "The last name must not be empty") @Email String email,
			@Size(min = 8, message = "Password must contain more than eight characters") String pass,
			Collection<RoleDTO> roles) {
		this.name = name;
		this.last_name = last_name;
		this.user = user;
		this.email = email;
		this.pass = pass;
		this.roles = roles;
	}

	public UserDTO() {
		super();
	}

}
