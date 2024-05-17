package com.team4.demo.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFindByNameDto {

	private Integer productId;

    private String productName;

    private Integer productPrice;

    private String productDescription;    

    private Integer inventoryQuantity;

    private byte[] productPhoto;
}