package com.team4.demo.model.repository;

import com.team4.demo.model.entity.Member;
import com.team4.demo.model.entity.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import com.team4.demo.model.entity.Coupon;


public interface MemberRepository extends JpaRepository<Member, Integer> {

    List<Member> findTop6ByEmailContaining(String email);
    
    List<Member> findMembersByCoupons_CouponId(Integer couponId);

    List<Member> findByPhone(String phone);

    @Query(nativeQuery = true, value = "SELECT * FROM Member WHERE email = :email")
    List<Member> findMemberByEmail(@Param("email") String email);
    
    @Query(nativeQuery = true, value = "SELECT * FROM Member WHERE email = :email And password = :password")
    Member findMemberByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    //	登入用
	public Optional<Restaurant> findByemail(String email);

    Member findByEmail(String email);

}
