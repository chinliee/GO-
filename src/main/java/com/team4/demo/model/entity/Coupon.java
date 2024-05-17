package com.team4.demo.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coupon")
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private Integer couponId;

	@Column(name = "code")
	private String code;

	@Column(name = "type")
	private String type;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "usage_price")
	private Integer usagePrice;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "end_time")
	private Date endTime;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "member_coupon", joinColumns = {
			@JoinColumn(name = "coupon_id", referencedColumnName = "coupon_id") }, inverseJoinColumns = {
					@JoinColumn(name = "member_id", referencedColumnName = "member_id") })
	private List<Member> members = new ArrayList<>();

	public void addMember(Member member) {
		this.members.add(member);
		member.getCoupons().add(this);
	}


	public void removeMember(Integer memberId) {
		Member member = this.members.stream().filter(m -> m.getMemberId() == memberId).findFirst().orElse(null);
		if(member != null) {
			this.members.remove(member);
			member.getCoupons().remove(this);
		}
	}

}
