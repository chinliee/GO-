package com.team4.demo.model.entity;

import org.springframework.stereotype.Component;

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
@Table(name = "restaurantphoto")
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_id")
	private Integer photoId;
	
	@Column(name = "photofile")
	private byte[] photoFile;
	
	@Column(name = "photomain")
	private Integer photoMain;
	
	@ManyToOne(fetch = FetchType.EAGER,targetEntity = Restaurant.class)
	@JoinColumn(name = "restaurant_id" )
	private Restaurant restaurant;

	
}
