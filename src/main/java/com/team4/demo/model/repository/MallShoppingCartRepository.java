package com.team4.demo.model.repository;

import com.team4.demo.model.entity.MallShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallShoppingCartRepository extends JpaRepository<MallShoppingCart, Integer> {
    // 這裡可以添加自定義的查詢方法
}
