package com.team4.demo.model.repository;

import com.team4.demo.model.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // Spring Data Jpa 還可以這樣查詢，Menu Entity 中的 @ManyToOne 關聯屬性 restaurant 物件
    // 根據 restaurantId，查詢出對應的 Menu 資料 (效果會和 Entity 沒有設計關聯屬性，用 findByRestaurantId 一樣)
    List<Menu> findByRestaurant_RestaurantId(Integer restaurantId);

    // 練習寫法，Menu Entity 中的 @ManyToOne 關聯屬性 restaurant，根據 restaurant 的 id 和 name 查詢菜單資料
    List<Menu> findByRestaurant_RestaurantIdAndRestaurant_Name(Integer restaurantId, String restaurantName);

    // 練習寫法
    List<Menu> findByRestaurant_RestaurantIdAndPriceGreaterThan(Integer restaurantId, Integer price);

}
