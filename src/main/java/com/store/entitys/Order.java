package com.store.entitys;

import java.sql.Date;
import java.util.Collection;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "secPhone", nullable = true, length = 11)
    private String secondPhone;
    @Column(name = "state", nullable = false)
    private int state;

    @Column(name = "sale_date", nullable = true)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = true)
    private Address address;

    @ManyToMany
    @JoinTable(name = "products_order", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonManagedReference
    private Collection<Product> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recep_id", nullable = true)
    private User recepcionist;

    /**
     * Creo esta variable aunque es derivable de la relación product-order
     * ya que el precio de los productos puede variar
     * y necesito tener lo recaudado según el precio de los productos
     * vigentes en el momento que se creo esta orden
     */
    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(String secondPhone) {
        this.secondPhone = secondPhone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getRecepcionist() {
        return recepcionist;
    }

    public void setRecepcionist(User recepcionist) {
        this.recepcionist = recepcionist;
    }

    public Order(String secondPhone, int state, Address address, User user,
            Collection<Product> products) {
        this.secondPhone = secondPhone;
        this.state = state;
        this.address = address;
        this.products = products;
    }

    public Order(int state, Address address, User user) {
        this.state = state;
        this.address = address;
    }

    public Order() {
    }

}
