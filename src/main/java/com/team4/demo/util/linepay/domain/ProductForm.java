package com.team4.demo.util.linepay.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
public class ProductForm {

    private String id;

    private String name;

    private String imageUrl;

    private BigDecimal price;

    private BigDecimal quantity;

}
