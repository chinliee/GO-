package com.team4.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.entity.MallShoppingCart;
import com.team4.demo.model.repository.MallShoppingCartRepository;

@Service
public class MallShoppingCartService {

	@Autowired
    private final MallShoppingCartRepository mallShoppingCartRepository;

    public MallShoppingCartService(MallShoppingCartRepository mallShoppingCartRepository) {
        this.mallShoppingCartRepository = mallShoppingCartRepository;
    }

    public List<MallShoppingCart> getAllMallShoppingCarts() {
        return mallShoppingCartRepository.findAll();
    }

}
