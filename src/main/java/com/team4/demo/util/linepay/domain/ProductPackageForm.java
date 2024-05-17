package com.team4.demo.util.linepay.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductPackageForm {

    private String id;

    private String name;

    private BigDecimal amount;

    private List<ProductForm> products;

}
