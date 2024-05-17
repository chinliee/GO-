package com.team4.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "restaurant")
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id")
	private Integer restaurantId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "uniformnumbers")
	private String uniformNumbers;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "salt")
	private String salt;
	
	@Column(name = "county")
	private String county;
	
	@Column(name = "district")
	private String district;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "longitude")
	private BigDecimal longitude;
	
	@Column(name = "latitude")
	private BigDecimal latitude;
	
	@Column(name = "open_time")
	private LocalTime openTime;
	
	@Column(name = "close_time")
	private LocalTime closeTime;
	
	@Column(name = "style")
	private String style;
	
	@Column(name = "introduce")
	private String introduce;

    // mappedBy，寫在一方(被控方)，mappedBy 的值是「多方 Java Entity 類別中關聯屬性的名稱」
    // @ToMany結尾的，fetch 預設是 LAZY；@ToOne結尾的，fetch 預設都是 EAGER
    // 使用 FetchMode.SUBSELECT，避免 N+1 查詢的效能問題
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = Menu.class)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuCart.class)
    @Fetch(value = FetchMode.SUBSELECT)

    private List<MenuCart> menuCartList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuOrder.class)
    @Fetch(value = FetchMode.SUBSELECT)

    private List<MenuOrder> menuOrderList = new ArrayList<>();
    
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = AdData.class)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<AdData> adDataList = new ArrayList<>();
    
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = {CascadeType.ALL},orphanRemoval = true, targetEntity = RestaurantRecord.class)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonIgnore
	private List<RestaurantRecord> restaurantRecordList = new ArrayList<>();
		
	@OneToMany(mappedBy = "restaurant",fetch = FetchType.LAZY,cascade = {CascadeType.ALL},orphanRemoval = true, targetEntity = RestaurantPhoto.class )
	@JsonIgnore
	private List<RestaurantPhoto> restaurantPhotosList = new ArrayList<>();
	
	@OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = {CascadeType.ALL},orphanRemoval = true, targetEntity = RestaurantSeat.class)
	@JsonIgnore
	private List<RestaurantSeat> restaurantSeatList  = new ArrayList<>();

	@OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = {CascadeType.ALL},orphanRemoval = true, targetEntity = RestaurantReserve.class)
	@JsonIgnore
	private List<RestaurantReserve> restaurantReservesList = new ArrayList<>();

}
