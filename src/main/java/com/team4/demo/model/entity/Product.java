package com.team4.demo.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team4.demo.model.dto.product.ProductDataDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "product_category_id")
    private Integer productCategoryId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_description", length = 3000)
    private String productDescription;

    @Column(name = "shelf_time")
    private Date shelfTime;

    @Column(name = "product_status")
    private Integer productStatus;

    @Column(name = "audit_status")
    private Integer auditStatus;

    @Column(name = "inventory_quantity")
    private Integer inventoryQuantity;
    
	@Column(name = "photo")
	private byte[] productPhoto;

    @ManyToOne(targetEntity = ProductCategory.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_category_id", insertable = false, updatable = false)
    private ProductCategory productCategory;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductPhoto.class)
    @JsonIgnore
    private List<ProductDataDto> productPhotoList = new ArrayList<>();


	
	

}










