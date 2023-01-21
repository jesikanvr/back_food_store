package com.store.dto;

import javax.validation.constraints.NotNull;

public class ProductRatingDTO {

	private Long id;
	@NotNull
	private Integer stars;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public ProductRatingDTO() {
		super();
	}

}
