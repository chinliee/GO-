package com.team4.demo.model.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
//	登入用
	public Optional<Restaurant> findByuniformNumbers(String uniformNumbers);
//	找
	Restaurant findByUniformNumbers(String uniformNumbers);
	 
//	findBy..... 需跟Bean(model) 所宣告的型別相同
//	List<Restaurant> findByName (String name);
	
	List<Restaurant> findBycounty (String county);
	
	List<Restaurant> findBydistrict(String district);
//	同一MODEL多查詢用ANd連接
	List<Restaurant> findByCountyAndDistrict(String county, String district);
//	檢查帳號是否重複
	boolean existsByUniformNumbers(String uniformNumbers);
	 
	List<Restaurant> findByCountyAndStyle(String county,String style);
		
//	@Modifying 註解為更新而不是查詢
//	Query原生SQL指令
// 	返回類型INT 表示資料庫受影響的行數。
	@Modifying
	@Query("UPDATE Restaurant r SET r.county = :county, r.district = :district, r.address = :address, r.phone = :phone, r.openTime = :openTime, r.closeTime = :closeTime, r.style = :style WHERE r.restaurantId = :restaurantId")
	@EntityGraph(attributePaths = {"lazyAttribute"})
	Integer updateRestaurantInfo(@Param("restaurantId") Integer restaurantId, @Param("county") String county, @Param("district") String district, @Param("address") String address, @Param("phone") String phone, @Param("openTime") LocalTime openTime, @Param("closeTime") LocalTime closeTime, @Param("style") String style);
	
}
