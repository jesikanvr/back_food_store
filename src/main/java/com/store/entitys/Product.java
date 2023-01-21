package com.store.entitys;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", nullable = false, length = 30)
	private String name;
	@Column(name = "price", nullable = false, length = 5)
	private double price;
	@Column(name = "description", nullable = false, length = 130)
	private String description;
	@Column(name = "imgUrl", nullable = false)
	private String imgUrl;
	@Column(name = "onSale", nullable = false)
	private Boolean onSale;

	@Column(name = "imgName", nullable = false)
	private String imgName;

	// Esta columna es para si el admin desea eliminar un producto
	// Al tener referencia con la tabla orders da error al eliminar un producto
	// Y si se elimina un producto se tendrán que eliminar todas las ordenes
	// relacionadas
	// y por las ordenes es que se saca el registro de ventas.
	@Column(name = "deleted", nullable = true)
	private Boolean deleted = false;

	// orphanRemoval elimina todos los objetos que dependen de otro para existir
	// cuando dicho objeto se eleimina
	@JsonBackReference // esta etiqueta corrige errores cuando hay referencias bidireccionales
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<ProductRating> ratings = new HashSet<>();

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Boolean getOnSale() {
		return onSale;
	}

	public void setOnSale(Boolean onSale) {
		this.onSale = onSale;
	}

	public Collection<ProductRating> getRatings() {
		return ratings;
	}

	public void setRatings(Collection<ProductRating> ratings) {
		this.ratings = ratings;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public Product(String name, double price, String description, String imgUrl, String imgName, Boolean onSale,
			Collection<ProductRating> ratings) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.imgUrl = imgUrl;
		this.imgName = imgName;
		this.onSale = onSale;
		this.ratings = ratings;
	}

	public Product(String name, double price, String description, String imgUrl, String imgName, Boolean onSale) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.imgUrl = imgUrl;
		this.imgName = imgName;
		this.onSale = onSale;
	}

	public Product() {
	}

}
