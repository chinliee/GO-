package com.team4.demo.model.repository;

import com.team4.demo.model.entity.MenuCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuCartRepository extends JpaRepository<MenuCart, Integer> {

    Optional<MenuCart> findByMember_MemberIdAndRestaurant_RestaurantId(Integer memberId, Integer restaurantId);

}
