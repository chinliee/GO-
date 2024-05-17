package com.team4.demo.model.entity;


import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurantreserve")
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantReserve {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reserved_id")
	private Integer reservedId;
			
	@Column(name = "reserveday")
	private LocalDate reserveDay;
	
	@Column(name = "reservetime")
	private LocalTime reserveTime;
	
	@Column(name = "customer")
	private Integer customer;
	
	@Column(name = " reservedstate")
	private String state;
	
	@Column(name = "amount")
	private Integer amount;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Restaurant.class)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity =  RestaurantSeat.class)
	@JoinColumn(name = "seat_id")
	private RestaurantSeat restaurantSeat;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity =  Member.class)
	@JoinColumn(name = "member_id")
	private Member member;
	


}
