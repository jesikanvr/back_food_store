package com.store.dto;

import java.sql.Date;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ReportDTO {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private int type;

    private Date startDate;
    @NotNull
    private Date endDate;
    @NotEmpty
    @NotNull
    private Collection<ProductDTO> products;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Collection<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(@NotEmpty @NotNull Collection<ProductDTO> products) {
        this.products = products;
    }

    public ReportDTO(Long id, @NotNull String name, @NotNull int type, Date startDate, @NotNull Date endDate,
            @NotEmpty @NotNull Collection<ProductDTO> products) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.products = products;
    }

    public ReportDTO() {
    }

}
