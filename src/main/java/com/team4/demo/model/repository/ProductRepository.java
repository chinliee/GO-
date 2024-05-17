package com.team4.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	//模糊搜尋商品分類名稱對應的商品
	

	List<Product> findByProductCategory_ProductCategoryNameAndProductNameContaining(String ProductCategoryName, String productName);

	//模糊搜尋商品名稱對應的商品
	
	List<Product> findByProductNameContaining(String productName);

//
//	Product findByProductName(String productName);
//
//	
	
//	
//	//合併兩個Table
//	@Query(value = "SELECT a FROM Product a left join Category b on a.productCategoryId = b.categoryId ")
//	List<Product> findAllWithCategory();
//	
}

			