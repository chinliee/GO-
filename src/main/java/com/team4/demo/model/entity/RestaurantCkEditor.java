package com.team4.demo.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurantckeditor")
public class RestaurantCkEditor {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private Integer id;
	    
	    @Column(name = "html")
	    private String html;

	    @OneToOne(cascade = {CascadeType.ALL},orphanRemoval = true, targetEntity = Restaurant.class)
	    @JoinColumn(name = "restaurant_id")
	    private Restaurant restaurant;
	    
}
