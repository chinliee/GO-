package com.team4.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team4.demo.exception.DataNotFoundException;
import com.team4.demo.model.dto.coupon.CouponCreateDto;
import com.team4.demo.model.dto.coupon.CouponFindDto;
import com.team4.demo.model.dto.coupon.CouponUpdateDto;
import com.team4.demo.model.dto.member.MemberSearchRespDto;
import com.team4.demo.model.entity.Coupon;
import com.team4.demo.model.entity.Member;
import com.team4.demo.model.repository.CouponRepository;
import com.team4.demo.model.repository.MemberRepository;

@Service
@Transactional
public class CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private MemberRepository memberRepository;
	
	public List<CouponFindDto> findByCodeContaining(String keyWord) {
		List<Coupon> findByCodeContaining = couponRepository.findByCodeContaining(keyWord);
		List<CouponFindDto> dtos = new ArrayList<>();
		for (Coupon coupon : findByCodeContaining) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public List<CouponFindDto> findByTypeContaining(String keyWord) {
		List<Coupon> findByTypeContaining = couponRepository.findByTypeContaining(keyWord);
		List<CouponFindDto> dtos = new ArrayList<>();
		for (Coupon coupon : findByTypeContaining) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public List<CouponFindDto> findByDiscountContaining(String keyWord) {
		List<Coupon> findByDiscountContaining = couponRepository.findByDiscountContaining(keyWord);
		List<CouponFindDto> dtos = new ArrayList<>();
		for (Coupon coupon : findByDiscountContaining) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public List<CouponFindDto> findByUsagePriceContaining(String keyWord) {
		List<Coupon> findByUsagePriceContaining = couponRepository.findByUsagePriceContaining(keyWord);
		List<CouponFindDto> dtos = new ArrayList<>();
		for (Coupon coupon : findByUsagePriceContaining) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public List<CouponFindDto> findByStartTimeContaining(String keyWord) {
		List<Coupon> findByStartTimeContaining = couponRepository.findByStartTimeContaining(keyWord);
		List<CouponFindDto> dtos = new ArrayList<>();
		for (Coupon coupon : findByStartTimeContaining) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public List<CouponFindDto> findByEndTimeContaining(String keyWord) {
		List<Coupon> findByEndTimeContaining = couponRepository.findByEndTimeContaining(keyWord);
		List<CouponFindDto> dtos = new ArrayList<>();
		for (Coupon coupon : findByEndTimeContaining) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public Coupon insertCoupon(CouponCreateDto dto) {
		Coupon coupon = Coupon.builder().code(dto.getCode()).type(dto.getType()).discount(dto.getDiscount())
				.usagePrice(dto.getUsagePrice()).startTime(new Date(dto.getStartTime()))
				.endTime(new Date(dto.getEndTime())).build();
		return couponRepository.save(coupon);

	}

	public Coupon updateCoupon(Integer couponId, CouponUpdateDto dto) {
        Coupon coupon = Coupon.builder().couponId(couponId).code(dto.getCode()).type(dto.getType())
                .discount(dto.getDiscount()).usagePrice(dto.getUsagePrice()).startTime(new Date(dto.getStartTime()))
                .endTime(new Date(dto.getEndTime())).members(dto.getMembers()).build();
		return couponRepository.save(coupon);

	}

	public List<CouponFindDto> getAllCoupons() {
		List<Coupon> coupons = couponRepository.findAll();
		List<CouponFindDto> dtos = new ArrayList<>();

		for (Coupon coupon : coupons) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public CouponFindDto getCouponById(Integer couponiId) {

		Coupon coupon = couponRepository.findById(couponiId)
				.orElseThrow(() -> new DataNotFoundException("Not found Coupon with couponId = " + couponiId));
		

		CouponFindDto findDto = new CouponFindDto();
		findDto.setCouponId(coupon.getCouponId());
		findDto.setCode(coupon.getCode());
		findDto.setType(coupon.getType());
		findDto.setDiscount(coupon.getDiscount());
		findDto.setUsagePrice(coupon.getUsagePrice());
		findDto.setStartTime(coupon.getStartTime());
		findDto.setEndTime(coupon.getEndTime());
		
		return findDto;

	}

	public void deleteCouponById(Integer couponId) {

		couponRepository.deleteById(couponId);

	}

	public void deleteBatchOfcoupons(List<Integer> couponIds) {

		couponRepository.deleteAllByIdInBatch(couponIds);

	}

	public List<MemberSearchRespDto> getMembersByCouponId(Integer couponId) {
		if (!couponRepository.existsById(couponId)) {
			throw new DataNotFoundException("Not found Coupon with couponId = " + couponId);
		}

		List<Member> members = memberRepository.findMembersByCoupons_CouponId(couponId);
		List<MemberSearchRespDto> dtos = new ArrayList<>();
		for (Member member : members) {
			MemberSearchRespDto findDto = new MemberSearchRespDto();
			findDto.setMemberId(member.getMemberId());
			findDto.setEmail(member.getEmail());
			findDto.setName(member.getName());
			findDto.setRegisteration_date(member.getRegisteration_date());
			dtos.add(findDto);
		}

		return dtos;
	}

	public List<CouponFindDto> getCouponsByMemberId(Integer memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw new DataNotFoundException("Not found Member with memberId = " + memberId);
		}
		List<Coupon> coupons = couponRepository.findCouponsByMembers_MemberId(memberId);

		List<CouponFindDto> dtos = new ArrayList<>();

		for (Coupon coupon : coupons) {
			CouponFindDto findDto = new CouponFindDto();
			findDto.setCouponId(coupon.getCouponId());
			findDto.setCode(coupon.getCode());
			findDto.setType(coupon.getType());
			findDto.setDiscount(coupon.getDiscount());
			findDto.setUsagePrice(coupon.getUsagePrice());
			findDto.setStartTime(coupon.getStartTime());
			findDto.setEndTime(coupon.getEndTime());

			dtos.add(findDto);
		}

		return dtos;
	}

	public void deleteMemberFromCoupon(Integer couponId, Integer memberId) {
		Coupon coupon = couponRepository.findById(couponId)
				.orElseThrow(() -> new DataNotFoundException("Not found coupon with couponid = " + couponId));

		coupon.removeMember(memberId);
		couponRepository.save(coupon);

	}

	public Member addCouponToMember(Integer couponId, Member memberRequest) {
		Member member = couponRepository.findById(couponId).map(coupon -> {
			Integer memberId = memberRequest.getMemberId();

			if (memberId != null) {
				Member existingMember = memberRepository.findById(memberId)
						.orElseThrow(() -> new DataNotFoundException("Not found Member with MemberId = " + memberId));
				coupon.addMember(existingMember);
				couponRepository.save(coupon);
				return existingMember;
			}

			// add and create new Member
			coupon.addMember(memberRequest);
			return memberRepository.save(memberRequest);
		}).orElseThrow(
				() -> new DataNotFoundException("Not found Member with MemberId = " + memberRequest.getMemberId()));
		return member;
	}

	public void addCouponToAllMembers(Integer couponId) {
		// 獲取指定ID的 Coupon 物件
		Coupon coupon = couponRepository.findById(couponId)
				.orElseThrow(() -> new DataNotFoundException("Not found coupon with couponid = " + couponId));

		// 獲取所有的成員
		List<Member> members = memberRepository.findAll();

		// 將 Coupon 添加到每個成員的 Coupon 清單中
		for (Member member : members) {
			member.addCoupon(coupon);
			memberRepository.save(member);
		}
	}
}
