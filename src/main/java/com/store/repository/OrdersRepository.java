package com.store.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.entitys.Address;
import com.store.entitys.Order;
import com.store.entitys.User;

public interface OrdersRepository extends JpaRepository<Order, Long> {
        @Query(value = "SELECT MIN([sale_date]) FROM [dbo].[orders]", nativeQuery = true)
        public Date getMinDateSale();

        @Query(value = "IF not exists ("
                        +
                        "SELECT * FROM INFORMATION_SCHEMA.COLUMNS "
                        +
                        "WHERE COLUMN_NAME = 'amount' AND TABLE_NAME = 'products_order') "
                        +
                        "BEGIN "
                        +
                        "ALTER TABLE products_order ADD amount numeric(18)"
                        +
                        "END", nativeQuery = true)
        public void crearAmountColumn();

        @Query(value = "IF not exists ("
                        +
                        "SELECT * FROM INFORMATION_SCHEMA.COLUMNS "
                        +
                        "WHERE COLUMN_NAME = 'net_price' AND TABLE_NAME = 'products_order') "
                        +
                        "BEGIN "
                        +
                        "ALTER TABLE products_order ADD net_price numeric(18)"
                        +
                        "END", nativeQuery = true)
        public void crearNetPriceColumn();

        public List<Order> findByRecepcionist(User recepcionist);

        public List<Order> findByAddress(Address address);
}