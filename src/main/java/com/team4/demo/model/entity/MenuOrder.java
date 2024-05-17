package com.team4.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "menu_order")
public class MenuOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_fee")
    private Integer deliveryFee;

    @Column(name = "platform_fee")
    private Integer platformFee;

    @Column(name = "sub_total")
    private Integer subTotal;

    @Column(name = "total")
    private Integer total;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "status")
    private String status;

    @Column(name = "reservation_time")
    private Date reservationTime;

    @Column(name = "qrcode")
    private byte[] qrcode;

    @Column(name = "created_time")
    private Date createdTime;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Courier.class)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @OneToMany(mappedBy = "menuOrder", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, targetEntity = MenuOrderDetail.class)
    @Fetch(value= FetchMode.SUBSELECT)
    private List<MenuOrderDetail> orderDetailList = new ArrayList<>();

}
