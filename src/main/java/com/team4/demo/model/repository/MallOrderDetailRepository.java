package com.team4.demo.model.repository;

import com.team4.demo.model.entity.MallOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallOrderDetailRepository extends JpaRepository<MallOrderDetail, Integer> {
}
