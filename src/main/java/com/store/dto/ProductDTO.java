package com.store.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductDTO {
	private Long id;
	@NotBlank
	@NotEmpty // Este es una de las etiquetas para las validaciones
	@Size(min = 5, message = "The product name must have at least 5 characters")
	private String name;
	@NotNull
	private double price;

	@NotBlank
	@NotEmpty
	private String description;
	@NotBlank
	@NotEmpty
	@Size(min = 15, message = "The product URL image name must have at least 15 characters")
	private String imgUrl;
    
	@NotBlank
	@NotEmpty(message = "El nombre del archivo de imagen no puede estar vacio")
	private String imgName;

	@NotNull
	private Boolean onSale;

	private Double rating;

	private Boolean deleted;

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

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public ProductDTO() {
		super();
	}

	public ProductDTO(
			@NotEmpty @Size(min = 5, message = "The product name must have at least 5 characters") String name,
			@NotNull double price, String description,
			@NotEmpty @Size(min = 15, message = "The product URL image name must have at least 15 characters") String imgUrl,
			@NotEmpty(message = "El nombre del archivo de imagen no puede estar vacio") String imgName,
			@NotNull Boolean onSale) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.imgUrl = imgUrl;
		this.imgName = imgName;
		this.onSale = onSale;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
