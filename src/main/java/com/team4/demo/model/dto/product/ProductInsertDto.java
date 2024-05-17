package com.team4.demo.model.dto.product;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInsertDto {

    private Integer restaurantId;

    private Integer productCategoryId;

    private String productName;

    private Integer productPrice;

    private String productDescription;

    private Long shelfTime;

    private Integer productStatus;

    private Integer auditStatus;

    private Integer inventoryQuantity;
    
    private MultipartFile productPhoto;    
}