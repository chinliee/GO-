package com.team4.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.MallOrder;

@Repository
public interface MallOrderRepository extends JpaRepository<MallOrder, Integer> {


	List<MallOrder> findByMember_MemberId(Integer memberId);
	
	

}
