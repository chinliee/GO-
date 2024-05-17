package com.team4.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

	/*
	 * //透過分類名稱查詢對應此種商品分類的商品，模糊查询(productCategory在資料表就是商品分類的名稱，nvarchar型別) public
	 * List<Product> findProductByProductCategory(String productCategory);
	 */
	
	/*
	 * //透過分類名稱查詢對應此種商品分類的商品，模糊查询(productCategory在資料表就是商品分類的名稱，nvarchar型別)
	 * 
	 * @Query("SELECT p FROM Product p JOIN p.productCategory pc WHERE pc.productCategory LIKE %:categoryName%"
	 * ) List<Product> findByProductCategoryName(@Param("categoryName") String
	 * categoryName);
	 */
}

