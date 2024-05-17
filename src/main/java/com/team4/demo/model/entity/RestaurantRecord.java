package com.team4.demo.model.entity;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="restaurantrecord")
public class RestaurantRecord {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="restaurantrecord_id")
	private Integer restaurantrecordId;

	@Column(name ="timeper")
	private String timePer;
	
	
	@Column(name ="starttime")
	private LocalTime startTime;
	
	@Column(name ="endtime")
	private LocalTime endTime;
	
	@Column(name ="price")
	private Integer price;
	
	@Column(name ="note")
	private String note;
	
	// JoinColumn，name為外鍵名稱
    // ToOne結尾的，fetch 預設是 EAGER；ToMany結尾的，fetch 預設 LAZY
    // referencedColumnName(一般省略) 參考主鍵列的名稱	 
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Restaurant.class)
	@JoinColumn(name = "restaurant_id", referencedColumnName = "restaurant_id")
//	@JsonIgnore
	private Restaurant restaurant;

	@OneToMany(mappedBy = "restaurantRecord",fetch = FetchType.LAZY,cascade = {CascadeType.ALL},orphanRemoval = true, targetEntity = RestaurantSeat.class)
	@JsonIgnore
	private List<RestaurantSeat> restaurantSeatList = new ArrayList<>();


}
