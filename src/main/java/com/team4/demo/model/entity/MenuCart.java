package com.team4.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "menu_cart")
public class MenuCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "delivery_fee")
    private Integer deliveryFee;

    @Column(name = "platform_fee")
    private Integer platformFee;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menuCart", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuCartItem.class)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MenuCartItem> itemList = new ArrayList<>();

}
