package com.store.dto;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;

import io.micrometer.core.lang.NonNull;

public class OrderDTO {

    private Long id;
    @NonNull
    private Collection<Long> addresses;
    @NonNull
    private Map<Long, Integer> products;
    private String secondPhone;

    private int state;

    private Date date;

    private AddressDTO address;

    private UserDTO client;

    private UserDTO recepcionist;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @NonNull
    private int totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Long> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<Long> addresses) {
        this.addresses = addresses;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(String secondPhone) {
        this.secondPhone = secondPhone;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderDTO(Collection<Long> addresses, Map<Long, Integer> products, String secondPhone, int totalPrice) {
        this.addresses = addresses;
        this.products = products;
        this.secondPhone = secondPhone;
        this.totalPrice = totalPrice;
    }

    public OrderDTO() {
    }

    public UserDTO getClient() {
        return client;
    }

    public void setClient(UserDTO client) {
        this.client = client;
    }

    public UserDTO getRecepcionist() {
        return recepcionist;
    }

    public void setRecepcionist(UserDTO recepcionist) {
        this.recepcionist = recepcionist;
    }

}
