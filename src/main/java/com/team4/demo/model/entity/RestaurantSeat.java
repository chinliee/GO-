package com.team4.demo.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="restaurantseat")
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id")
	private Integer seatId;
	
	@Column(name = "tablefor")
	private String tableFor;
	
	@Column(name = "seattimeper")
	private String seatTimePer;
	
	@Column(name = "opentime")
	private LocalTime openTime;
	
	@Column(name = "openday")
	private LocalDate openDay;
	
	@Column(name = "seatstate")
	private String seatState;

	@OneToMany(mappedBy = "restaurantSeat",fetch = FetchType.LAZY,orphanRemoval = false, targetEntity = RestaurantReserve.class)
	@JsonIgnore
	private List<RestaurantReserve> restaurantReserveList = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = RestaurantRecord.class)
	@JoinColumn(name = "restaurantrecord_id", referencedColumnName = "restaurantrecord_id")
//	@JsonIgnore
	private RestaurantRecord restaurantRecord;
	
	
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Restaurant.class)
	@JoinColumn(name = "restaurant_Id" )
//	@JsonIgnore
	private Restaurant restaurant;


}
