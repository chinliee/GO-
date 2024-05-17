package com.team4.demo.model.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.Coupon;


@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	List<Coupon> findCouponsByMembers_MemberId(Integer memberId);
	
	List<Coupon> findByCodeContaining(String keyWord);
	
	List<Coupon> findByTypeContaining(String keyWord);
	
	@Query("SELECT c FROM Coupon c WHERE CAST(c.discount AS string) LIKE %?1%")
	List<Coupon> findByDiscountContaining(String keyWord);
	
	@Query("SELECT c FROM Coupon c WHERE CAST(c.usagePrice AS string) LIKE %?1%")
	List<Coupon> findByUsagePriceContaining(String keyWord);
	
	@Query("SELECT c FROM Coupon c WHERE FORMAT(c.startTime, 'yyyy-MM-dd HH:mm:ss') LIKE %?1%")
	List<Coupon> findByStartTimeContaining(String keyWord);
	
	@Query("SELECT c FROM Coupon c WHERE FORMAT(c.endTime, 'yyyy-MM-dd HH:mm:ss') LIKE %?1%")
	List<Coupon> findByEndTimeContaining(String keyWord);
			
}
	