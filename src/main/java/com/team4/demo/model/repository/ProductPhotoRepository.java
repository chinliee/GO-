package com.team4.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.ProductPhoto;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Integer> {
	ProductPhoto findByProductId(Integer productId);
}