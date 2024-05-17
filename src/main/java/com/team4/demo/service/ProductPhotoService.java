package com.team4.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.dto.product.ProductDataDto;
import com.team4.demo.model.entity.ProductPhoto;
import com.team4.demo.model.repository.ProductPhotoRepository;

@Service
public class ProductPhotoService {

	@Autowired
	private ProductPhotoRepository productPhotoRepository;

	public ProductPhotoService(ProductPhotoRepository productPhotoRepository) {
		this.productPhotoRepository = productPhotoRepository;
	}

	// 新增商品圖片
	public void addProductPhoto(ProductDataDto productDataDto) {
		ProductPhoto productPhoto = new ProductPhoto();
		productPhoto.setPhotoId(productDataDto.getPhotoId());
		productPhoto.setProductId(productDataDto.getProductId());
		productPhoto.setProductPhoto(productDataDto.getProductPhoto()); 
		// 將圖片轉換為 byte[] 格式儲存於productPhoto);
		productPhotoRepository.save(productPhoto);
	}
	
	/*
	 *     @Column(name = "photo_id")
    private Integer photoId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_photo", nullable = false)
    private byte[] productPhoto;
	 * 
	 * 
	 * public void addProductPhoto(Integer productId, byte[] imageBytes) {
        ProductPhoto productPhoto = new ProductPhoto();
        productPhoto.setProductId(productId);
        productPhoto.setImage(imageBytes);
        productPhotoRepository.save(productPhoto);
    }
	*/
	
	// 更新商品圖片
	
	

	// 根據產品ID查詢圖片
	public ProductPhoto getProductPhotoByProductId(Integer productId) {
		return productPhotoRepository.findByProductId(productId);
	}
	// 根據產品ID更新圖片

	// 刪除產品圖片
	public void deleteProductPhoto(ProductPhoto productPhoto) {
		productPhotoRepository.delete(productPhoto);
	}

	// 再圖片上傳、處理等
}
