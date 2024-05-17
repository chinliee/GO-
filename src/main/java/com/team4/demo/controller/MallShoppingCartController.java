package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.entity.MallShoppingCart;
import com.team4.demo.service.MallShoppingCartService;

@RestController
@RequestMapping("/mall-shopping-carts")
public class MallShoppingCartController {

	@Autowired
    private MallShoppingCartService mallShoppingCartService;

    public MallShoppingCartController(MallShoppingCartService mallShoppingCartService) {
        this.mallShoppingCartService = mallShoppingCartService;
    }

    @GetMapping
    public List<MallShoppingCart> getAllMallShoppingCarts() {
        return mallShoppingCartService.getAllMallShoppingCarts();
    }

}
