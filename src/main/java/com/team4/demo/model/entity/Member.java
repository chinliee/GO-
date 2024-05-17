package com.team4.demo.model.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "county")
    private String county;

    @Column(name = "district")
    private String district;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private String status;

    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "registeration_date")
    private Date registeration_date;

    @Column(name = "bin")
    private String bin;

    @Column(name = "pan")
    private String pan;

    @Column(name = "luhn")
    private String luhn;

    @Column(name = "existence")
    private String existence;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuCart.class)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MenuCart> menuCartList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuOrder.class)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MenuOrder> menuOrderList = new ArrayList<>();
    
    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private List<Coupon> coupons = new ArrayList<>();
    
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = {CascadeType.ALL},orphanRemoval = true, targetEntity = RestaurantReserve.class)
	@JsonIgnore
	private List<RestaurantReserve> restaurantReservesList = new ArrayList<>();
    
    public void addCoupon(Coupon coupon) {
        this.coupons.add(coupon);
        coupon.getMembers().add(this);
    }

    public void removeCoupon(Coupon coupon) {
        this.coupons.remove(coupon);
        coupon.getMembers().remove(this);
    }
}
