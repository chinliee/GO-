package com.team4.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team4.demo.model.dto.product.ProductFindByNameDto;
import com.team4.demo.model.dto.product.ProductInsertDto;
import com.team4.demo.model.dto.product.ProductUpdateDto;
import com.team4.demo.model.entity.Product;
import com.team4.demo.model.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	// 查詢全部商品
	public List<Product> findAllProducts() {
		return productRepository.findAll();
	}

	// 查詢單筆商品
	public Product findProductByProductId(Integer productId) {

		/*
		 * Product product = productRepository.findById(productId).orElse(null); return
		 * product; 下面的寫法也可以
		 */
		return productRepository.findById(productId).orElse(null);
	}

	// 模糊搜尋商品分類名稱對應的商品
	/*
	 * public List<ProductFindByNameDto> findProductByProductCategoryName(String
	 * ProductCategoryName, String productName) { List<Product> productlist =
	 * productRepository
	 * .findByProductCategory_ProductCategoryNameAndProductNameContaining(
	 * ProductCategoryName, productName); List<ProductFindByNameDto>
	 * productFindByNameDtos = new ArrayList<>();
	 * 
	 * for(Product product : productlist) { ProductFindByNameDto
	 * productFindByNameDto = new ProductFindByNameDto();
	 * productFindByNameDto.setProductId(product.getProductId());
	 * productFindByNameDto.setProductName(product.getProductName());
	 * productFindByNameDto.setProductPrice(product.getProductPrice());
	 * productFindByNameDto.setProductDescription(product.getProductDescription());
	 * productFindByNameDto.setInventoryQuantity(product.getInventoryQuantity());
	 * productFindByNameDto.setProductPhoto(product.getProductPhoto());
	 * productFindByNameDtos.add(productFindByNameDto); } return
	 * productFindByNameDtos; }
	 */
			
	//楓昱 商品名稱＋商品分類  可以寫在一起
	
	public List<ProductFindByNameDto> findProductByProductCategoryName(String ProductCategoryName, String productName) {
	
		List<Product> productlist;
		
	if(ProductCategoryName != null && productName != null) {
		productlist = productRepository
				.findByProductCategory_ProductCategoryNameAndProductNameContaining(ProductCategoryName, productName);
	}
	else {
		productlist = productRepository.findByProductNameContaining(productName);
	}
	
	List<ProductFindByNameDto> productFindByNameDtos = new ArrayList<>();
		
		
		for(Product product : productlist) {
			ProductFindByNameDto productFindByNameDto = new ProductFindByNameDto();
			productFindByNameDto.setProductId(product.getProductId());
			productFindByNameDto.setProductName(product.getProductName());
			productFindByNameDto.setProductPrice(product.getProductPrice());
			productFindByNameDto.setProductDescription(product.getProductDescription());
			productFindByNameDto.setInventoryQuantity(product.getInventoryQuantity());
			productFindByNameDto.setProductPhoto(product.getProductPhoto());
			productFindByNameDtos.add(productFindByNameDto);
		}
		return productFindByNameDtos;
	}
		

	// 刪除單筆商品
	public String deleteProduct(Integer productId) {
		productRepository.deleteById(productId);
		return "刪除成功";
	}
	/*
	 * public void deleteProduct(Integer productId) {
	 * productRepository.deleteById(productId); } 這邊是沒有回傳值的 上面是有回傳刪除成功
	 */

	// 新增單筆商品

	public Product createProduct(ProductInsertDto productInsertDto) throws IOException {
		Product product = new Product();
		product.setRestaurantId(productInsertDto.getRestaurantId());
		product.setProductCategoryId(productInsertDto.getProductCategoryId());
		product.setProductName(productInsertDto.getProductName());
		product.setProductPrice(productInsertDto.getProductPrice());
		product.setProductDescription(productInsertDto.getProductDescription());
		product.setShelfTime(new Date(productInsertDto.getShelfTime()));
		product.setProductStatus(productInsertDto.getProductStatus());
		product.setAuditStatus(productInsertDto.getAuditStatus());
		product.setInventoryQuantity(productInsertDto.getInventoryQuantity());
		product.setProductPhoto(productInsertDto.getProductPhoto().getBytes());
//		product.setProductPhotoList(productDataDto.getProductList());
		return productRepository.save(product);
	}

	// 修改單筆商品

	public Product updateProduct(ProductUpdateDto productDto) throws IOException {
		Product product = new Product();
		product.setProductId(productDto.getProductId());
		product.setRestaurantId(productDto.getRestaurantId());
		product.setProductCategoryId(productDto.getProductCategoryId());
		product.setProductName(productDto.getProductName());
		product.setProductPrice(productDto.getProductPrice());
		product.setProductDescription(productDto.getProductDescription());
		product.setShelfTime(new Date(productDto.getShelfTime()));
		product.setProductStatus(productDto.getProductStatus());
		product.setAuditStatus(productDto.getAuditStatus());
		product.setInventoryQuantity(productDto.getInventoryQuantity());
		product.setProductPhoto(productDto.getProductPhoto().getBytes());
//		product.setProductPhotoList(productDataDto.getProductList());
		return productRepository.save(product);
	}

//
//	public Product saveProduct(Product product) {
//
//		return productRepository.save(product);
//
//	}
//
//
//	public Product getProductByProductName(String productName) {
//
//		return productRepository.findByProductName(productName);
//
//	}
//

//
//	public ProductPhoto saveProductPhoto(ProductPhoto productPhoto) {
//		return null;
//	}
//
//	public List<ProductPhoto> getAllProductPhotos() {
//		return null;
//	}

}
