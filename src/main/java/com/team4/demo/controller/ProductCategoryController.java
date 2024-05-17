package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.entity.ProductCategory;
import com.team4.demo.service.ProductCategoryService;

@RestController
@RequestMapping("/product-categories")
public class ProductCategoryController {

	@Autowired
    private ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    // 其他路由和操作，如新增、更新、刪除等
}
