package com.store.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.micrometer.core.lang.NonNull;

public class AddressDTO {

	private Long id;
	@NotBlank
	@NonNull
	private String formatted;
	@NonNull
	private MunicipalitiesDTO municipalities;
	private Float latitude;
	private Float longitude;
	@NotBlank
	@NonNull
	private String alias;
	@NotBlank
	@NotNull
	private String apto;

	private Boolean isValid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFormatted() {
		return formatted;
	}

	public void setFormatted(String formatted) {
		this.formatted = formatted;
	}

	public MunicipalitiesDTO getMunicipalities() {
		return municipalities;
	}

	public void setMunicipalities(MunicipalitiesDTO municipalities) {
		this.municipalities = municipalities;
	}

	public String getApto() {
		return apto;
	}

	public void setApto(String apto) {
		this.apto = apto;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public AddressDTO(String formatted, String apto, Float latitude, Float longitude, String alias) {
		super();
		this.formatted = formatted;
		this.latitude = latitude;
		this.longitude = longitude;
		this.alias = alias;
		this.apto = apto;
	}

	public AddressDTO() {
		super();
	}

}
