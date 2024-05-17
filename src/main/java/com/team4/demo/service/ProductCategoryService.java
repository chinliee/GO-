package com.team4.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.entity.Product;
import com.team4.demo.model.entity.ProductCategory;
import com.team4.demo.model.repository.ProductCategoryRepository;

@Service
public class ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	/*
	 * 透過查詢關鍵字，關鍵字中若含有分類名稱，要找出有對應到此種商品分類的商品，進而呈現在前端畫面，模糊查询(
	 * productCategory在資料表就是商品分類的名稱，nvarchar型別)
	 */
	/*
	 * public List<Product> findProductByProductCategory(String productCategory) {
	 * productCategory = "%" + productCategory + "%";
	 * System.out.println(productCategory);
	 * 
	 * return
	 * ProductCategoryRepository.findProductByProductCategory(productCategory).
	 * orEelse(null);
	 * 
	 * }
	 */

	/*
	 * public List<Product> findByProductCategoryName(String categoryName) {
	 * System.out.println(categoryName); categoryName = "%" + categoryName + "%";
	 * return ProductCategoryRepository.findByProductCategoryName(categoryName ); }
	 */


	
	
	
	
	public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
		this.productCategoryRepository = productCategoryRepository;
	}

	public List<ProductCategory> getAllProductCategories() {
		return productCategoryRepository.findAll();
	}

}
