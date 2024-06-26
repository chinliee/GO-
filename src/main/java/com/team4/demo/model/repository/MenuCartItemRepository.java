package com.team4.demo.model.repository;

import com.team4.demo.model.entity.MenuCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuCartItemRepository extends JpaRepository<MenuCartItem, Integer> {

}
