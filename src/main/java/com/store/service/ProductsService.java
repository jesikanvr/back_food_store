package com.store.service;

import java.sql.Date;
import java.util.List;

import com.store.dto.ProductDTO;
import com.store.dto.ProductRatingDTO;
import com.store.dto.ProductsResponse;

public interface ProductsService {
	public ProductDTO createProduct(ProductDTO productDTO);

	public ProductsResponse getAllProducts(ProductDTO product, String[] ignorePaths, int pageNum, int pageSize, String sortBy, String sortDir, String filter);

	public List<ProductDTO> getOnSaleProducts();

	public List<ProductDTO> getNotDeletedProducts();

	public List<ProductDTO> getProductsByOrder(Long orderId);

	public ProductDTO findById(Long id);

	public ProductDTO updateProduct(ProductDTO productDTO, Long id);

	public void deleteProduct(Long id);

	public Double getRatingsByProductId(Long productId);

	public Boolean IsProductEvaluatedByUser(Long userId, Long productId);

	public ProductRatingDTO createRating(Long userId, Long productId, ProductRatingDTO ratingDTO);

	public List<ProductDTO> findAll(String keyWord);

	public List<ProductDTO> findFavorites(Long userId);

	public List<ProductDTO> findTheMostSold();

	public Integer getImgNames(String name);

	public Double getTotalSales(Long productId, Date start, Date end);
}
