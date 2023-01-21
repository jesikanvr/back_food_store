package com.store.repository;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.entitys.Product;
import com.store.entitys.User;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

        public List<Product> findByDeleted(Boolean value);

        public List<Product> findByOnSale(Boolean value);

        @Query(value = "SELECT p.* FROM [dbo].[products] as p WHERE p.on_sale=1 AND p.name LIKE %?1% OR p.description LIKE %?1%", nativeQuery = true)
        public List<Product> findAll(String keyWord);

        @Query(value = "SELECT p.* FROM products p JOIN ratings pr ON p.id = pr.product_id WHERE p.on_sale=1 AND pr.user_id = ?1 AND pr.stars = 5", nativeQuery = true)
        public List<Product> findAll(User user);

        @Modifying
        @Transactional
        @Query(value = "UPDATE products_order SET products_order.amount = :amount WHERE products_order.order_id = :order_id AND products_order.product_id = :product_id", nativeQuery = true)
        public void insertAmount(@Param("amount") Integer amount, @Param("order_id") Long orderId,
                        @Param("product_id") Long productId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE products_order SET products_order.net_price = :netPrice WHERE products_order.order_id = :order_id AND products_order.product_id = :product_id", nativeQuery = true)
        public void insertNetPrice(@Param("netPrice") Double netPrice, @Param("order_id") Long orderId,
                        @Param("product_id") Long productId);

        @Query(value = "WITH RPA AS " +
                        "(" +
                        "SELECT product_id, SUM(amount) AS sa FROM products_order GROUP BY product_id" +
                        ")" +
                        "SELECT TOP(10) pr.* FROM products pr JOIN RPA rpa ON pr.id = rpa.product_id WHERE rpa.sa >= (SELECT AVG(rpa.sa) FROM RPA)", nativeQuery = true)
        public List<Product> findAllMostSold();

        public List<Product> findByImgNameAndDeleted(String name, Boolean deleted);

        @Query(value = "SELECT sum([dbo].[products_order].net_price) FROM [dbo].[products_order] JOIN [dbo].[orders] ON [dbo].[products_order].order_id = [dbo].[orders].id "
                        +
                        "WHERE [dbo].[orders].sale_date >= ?2 AND [dbo].[orders].sale_date <= ?3 AND [dbo].[products_order].product_id = ?1", nativeQuery = true)
        public Double getNetPrice(Long productId, Date start, Date end);

        @Query(value = "SELECT amount FROM products_order po WHERE po.order_id = ?1 AND po.product_id = ?2 ", nativeQuery = true)
        public Integer getAmount(Long orderId, Long productId);

        @Query(value = "SELECT product_id FROM products_order po WHERE po.order_id = ?1 ", nativeQuery = true)
        public List<Long> getProductsByOrder(Long orderId);

        @Query(value = "SELECT pr.* FROM products pr JOIN products_order po ON pr.id = po.product_id "
                        +
                        "WHERE po.order_id = ?1", nativeQuery = true)
        public List<Product> findProductsByOrder(Long orderId);

        /**
         * Hay que hacer algo así para optimizar las consultas para hacer los reportes
         * pero por ahora esto no funciona, aunque es la idea
         */
        // @Query(value = "SELECT [dbo].[products].*,
        // SUM([dbo].[products_order].net_price) FROM [dbo].[products_order] JOIN
        // [dbo].[orders] ON [dbo].[products_order].order_id = [dbo].[orders].id"
        // +
        // "JOIN [dbo].[products] ON [dbo].[products].id =
        // [dbo].[products_order].product_id "
        // +
        // "WHERE [dbo].[orders].sale_date >= ?2 AND [dbo].[orders].sale_date <= ?3 AND
        // [dbo].[products_order].product_id IN (?1)"
        // +
        // "GROUP BY [dbo].[products].name", nativeQuery = true)
        // public List<SalesProduct> getSaleNetPrice(Long[] productIds, Date start, Date
        // end);
}