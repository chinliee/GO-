package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.coupon.CouponCreateDto;
import com.team4.demo.model.dto.coupon.CouponFindDto;
import com.team4.demo.model.dto.coupon.CouponUpdateDto;
import com.team4.demo.model.dto.member.MemberSearchRespDto;
import com.team4.demo.model.entity.Coupon;
import com.team4.demo.model.entity.Member;
import com.team4.demo.service.CouponService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CouponController {

	@Autowired
	private CouponService couponService;

	
	@GetMapping("/coupons/search/{attribute}/{keyWord}")
	public ResponseEntity<List<CouponFindDto>> getCouponsByAttributeAndKeyWord(@PathVariable String attribute,
			@PathVariable String keyWord) {
		List<CouponFindDto> coupons;

		switch (attribute) {
		case "code":
			coupons = couponService.findByCodeContaining(keyWord);
			break;
		case "type":
			coupons = couponService.findByTypeContaining(keyWord);
			break;
		case "discount":
			coupons = couponService.findByDiscountContaining(keyWord);
			break;
		case "usagePrice":
			coupons = couponService.findByUsagePriceContaining(keyWord);
			break;
		case "startTime":
			coupons = couponService.findByStartTimeContaining(keyWord);
			break;
		case "endTime":
			coupons = couponService.findByEndTimeContaining(keyWord);
			break;
		default:
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (coupons.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(coupons, HttpStatus.OK);
	}

	@GetMapping("/coupons")
	public ResponseEntity<List<CouponFindDto>> getAllCoupons() {
		List<CouponFindDto> coupons = couponService.getAllCoupons();

		if (coupons.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(coupons, HttpStatus.OK);
	}

	@GetMapping("/coupons/{couponId}")
	public ResponseEntity<CouponFindDto> getCouponById(@PathVariable("couponId") Integer couponId) {
		CouponFindDto coupon = couponService.getCouponById(couponId);

		return new ResponseEntity<>(coupon, HttpStatus.OK);
	}

	@GetMapping("/coupons/{couponId}/members")
	public ResponseEntity<List<MemberSearchRespDto>> getMembersByCouponId(@PathVariable(value = "couponId") Integer couponId) {
		List<MemberSearchRespDto> members = couponService.getMembersByCouponId(couponId);
		return new ResponseEntity<>(members, HttpStatus.OK);
	}

	@GetMapping("/members/{memberId}/coupons")
	public ResponseEntity<List<CouponFindDto>> getCouponsByMemberId(@PathVariable(value = "memberId") Integer memberId) {
		List<CouponFindDto> coupons = couponService.getCouponsByMemberId(memberId);
		return new ResponseEntity<>(coupons, HttpStatus.OK);
	}

	@PostMapping("/coupons")
	public ResponseEntity<Coupon> insertCoupon(@RequestBody CouponCreateDto dto) {
		log.info("CouponController - insertCoupon ... dto => {}", dto);
		Coupon coupon = couponService.insertCoupon(dto);
		return new ResponseEntity<>(coupon, HttpStatus.CREATED);
	}

	// 新增coupon到member
	@PostMapping("/coupons/{couponId}/members")
	public ResponseEntity<Member> addCouponToMember(@PathVariable(value = "couponId") Integer couponId,
			@RequestBody Member memberRequest) {
		Member member = couponService.addCouponToMember(couponId, memberRequest);
		System.out.println(memberRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/coupons/{couponId}/toallmembers")
	public ResponseEntity<HttpStatus> addCouponToAllMembers(@PathVariable("couponId") Integer couponId) {
		couponService.addCouponToAllMembers(couponId);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/coupons/{couponId}")
	public ResponseEntity<HttpStatus> deleteCouponById(@PathVariable("couponId") Integer couponId) {
		couponService.deleteCouponById(couponId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/coupons/deleteBatch")
	public ResponseEntity<HttpStatus> deleteBatchOfcoupons(@RequestBody List<Integer> couponIds) {
		try {

			couponService.deleteBatchOfcoupons(couponIds);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/coupons/{couponId}/members/{memberId}")
	public ResponseEntity<HttpStatus> deleteMemberFromCoupon(@PathVariable(value = "couponId") Integer couponId,
			@PathVariable(value = "memberId") Integer memberId) {
		couponService.deleteMemberFromCoupon(couponId, memberId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/coupons/{couponId}")
	public ResponseEntity<Coupon> updateCouponById(@PathVariable Integer couponId, @RequestBody CouponUpdateDto dto) {
		Coupon coupon = couponService.updateCoupon(couponId, dto);
		return new ResponseEntity<>(coupon, HttpStatus.OK);
	}

}
