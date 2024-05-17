package com.team4.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.product.ProductFindByNameDto;
import com.team4.demo.model.dto.product.ProductInsertDto;
import com.team4.demo.model.dto.product.ProductUpdateDto;
import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.model.entity.Product;
import com.team4.demo.service.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {

	@Autowired
	private ProductService productService;

	// 查詢全部
	@GetMapping("/all")
	@ResponseBody
	public ResponseEntity<?> findAll() {
		List<Product> allProducts = productService.findAllProducts();
		return new ResponseEntity<>(allProducts, HttpStatus.OK);
	}

	// 透過查詢關鍵字，關鍵字中若含有分類名稱，要找出有對應到此種商品分類的商品，進而呈現在前端畫面，模糊查询(productCategory在資料表就是商品分類的名稱，nvarchar型別)

	@GetMapping("/CategoryName")
	public ResponseEntity<List<ProductFindByNameDto>> findProductByProductCategoryName(@RequestParam(value = "productCategory", required = false) String ProductCategoryName,@RequestParam("productName") String productName) {
		
		return new ResponseEntity<>(productService.findProductByProductCategoryName(ProductCategoryName, productName), HttpStatus.OK);

	}
	

	/*
	 * @GetMapping("/Category/search") public List<Product>
	 * searchProductsByCategoryName(@RequestParam("keyword") String keyword) {
	 * 
	 * 
	 * return productCategoryService.findProductsByCategoryName(keyword); }
	 */

	// 查詢單筆
	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductByProductId(@PathVariable("productId") Integer productId) {
		return new ResponseEntity<>(productService.findProductByProductId(productId), HttpStatus.OK);
	}

//	@DeleteMapping("/delete/{productId}")
//	public void deleteProduct(@PathVariable("productId") Integer productId) {
//		productService.deleteProduct(productId);
//	}

	// 刪除單筆
	@DeleteMapping("/delete/{productId}")
	public String deleteProduct(@PathVariable("productId") Integer productId) {
		return productService.deleteProduct(productId);
//		return "刪除成功Controller";
	}

	// 新增單筆商品
	@PostMapping("/create")

	public ResponseEntity<ResponseDto> createProduct(@ModelAttribute ProductInsertDto createIDto) throws IOException {
		System.out.println(createIDto);
		Product product = productService.createProduct(createIDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.value(), product));
	}

	// 修改單筆商品
	@PostMapping("/update")
	public ResponseEntity<ResponseDto> updateProduct(@ModelAttribute ProductUpdateDto updateDto) throws IOException {
		System.out.println(updateDto);
		Product product = productService.updateProduct(updateDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.value(), product));

	}

	/*
	 * // 新增單筆商品
	 * 
	 * @PostMapping("/save") public Product saveProduct(@RequestBody
	 * ProductInsertDto productIDto, ProductDataDto productDDto) {
	 * 
	 * return productService.saveProduct(productIDto, productDDto); }
	 */

	/*
	 * // 批次新增商品
	 * 
	 * @PostMapping("/batchSave") public ResponseEntity<String>
	 * batchSaveProducts(@RequestBody List<ProductInsertDto> productIDtos,
	 * List<ProductDataDto> productDDtos) { for (ProductInsertDto productDtos :
	 * productIDtos) { productService.saveProduct(productDtos, null); } return new
	 * ResponseEntity<>("批次新增成功", HttpStatus.OK); }
	 */

//	@GetMapping("/category")
//	public Product getProductByProductCategoryId(Integer productCategoryId) {
//		return productService.getProductByProductCategoryId(productCategoryId);
//	}
//
//	@GetMapping("/category/name")
//	public Product getProductByProductName(String productName) {
//		return productService.getProductByProductName(productName);
//	}
//
//
//
//	@PutMapping("/update")
//	public Product updateProduct(Product product) {
//
//		return productService.saveProduct(product);
//	}
//
//	@GetMapping("/photo")
//	public Product getProductPhotoByProductId(Long productId) {
//
//		return productService.getProductByProductId(productId);
//	}
//
//	@PostMapping("/photo/upload")
//	public ProductPhoto saveProductPhoto(ProductPhoto productPhoto) {
//		return productService.saveProductPhoto(productPhoto);
//	}
//
//
//	@GetMapping("/photo/update")
//	public ProductPhoto updateProductPhoto(ProductPhoto productPhoto) {
//		return productService.saveProductPhoto(productPhoto);
//	}
//
//	@GetMapping("/photo/delete")
//	public void deleteProductPhoto(ProductPhoto productPhoto) {
//
//	}
//
//	@GetMapping("/photo/delete/all")
//	public void deleteAllProductPhotos() {
//
//	}

}
