package com.team4.demo.model.repository;

import com.team4.demo.model.entity.MenuOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuOrderDetailRepository extends JpaRepository<MenuOrderDetail, Integer>, JpaSpecificationExecutor<MenuOrderDetail> {

}
