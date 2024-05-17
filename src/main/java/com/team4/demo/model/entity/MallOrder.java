package com.team4.demo.model.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mall_order")
public class MallOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "order_total_amount", nullable = false)
    private Integer orderTotalAmount;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "order_time", nullable = false)
    private Date orderTime;

    @Column(name = "payment_time", nullable = false)
    private Date paymentTime;

    @Column(name = "shipping_time", nullable = false)
    private Date shippingTime;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    
    @OneToMany(mappedBy = "mallOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MallOrderDetail.class)
	@JsonIgnore
	private List<MallOrderDetail> mallOrderDetails;
	
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member.class)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;
}
