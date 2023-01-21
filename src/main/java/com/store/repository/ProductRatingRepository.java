package com.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.entitys.ProductRating;

public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {
	public List<ProductRating> findByProductId(Long id);

	public List<ProductRating> findByUserIdAndProductId(Long userId, Long productId);
}
