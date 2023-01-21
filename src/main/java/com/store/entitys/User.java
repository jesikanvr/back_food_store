package com.store.entitys;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", nullable = false, length = 15)
	private String name;
	@Column(name = "last_name", nullable = false, length = 35)
	private String last_name;
	@Column(name = "username", nullable = false, length = 40)
	private String user;
	@Column(name = "email", nullable = false, length = 50)
	private String email;
	@Column(name = "password", nullable = false)
	private String pass;

	@Column(name = "token", nullable = true)
	private String token;

	/**
	 * Atributos opcionales
	 */
	@Column(name = "phone", nullable = true, length = 15)
	private String phone;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "users_rols", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
	@JsonManagedReference
	private Collection<Rol> roles;

	@JsonBackReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<Address> addresses = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mun_id", nullable = true)
	private Municipalities municipalities;

	@JsonBackReference
	@OneToMany(mappedBy = "recepcionist", cascade = CascadeType.PERSIST)
	private Collection<Order> orders = new HashSet<>();

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

	public Collection<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Rol> roles) {
		this.roles = roles;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Collection<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<Address> addresses) {
		this.addresses = addresses;
	}

	public String getToken_pass() {
		return token;
	}

	public void setToken_pass(String token) {
		this.token = token;
	}

	public Municipalities getMunicipalities() {
		return municipalities;
	}

	public void setMunicipalities(Municipalities municipalities) {
		this.municipalities = municipalities;
	}

	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

	public User(Long id, String name, String last_name, String user, String email, String pass, Collection<Rol> roles) {
		super();
		this.id = id;
		this.name = name;
		this.last_name = last_name;
		this.user = user;
		this.email = email;
		this.pass = pass;
		this.roles = roles;
	}

	public User(String name, String last_name, String user, String email, String pass) {
		super();
		this.name = name;
		this.last_name = last_name;
		this.user = user;
		this.email = email;
		this.pass = pass;
	}

	public User(String email) {
		super();
		this.email = email;
	}

	public User() {
		super();
	}

	public User orElseThrow(Object object) {
		return null;
	}

}