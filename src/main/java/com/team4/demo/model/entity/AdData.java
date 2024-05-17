package com.team4.demo.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ad_data")
public class AdData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ad_id")
	private Integer adId;
		
	@Column(name = "location")
	private String location;
	
	@Column(name = "photo")
	private byte[] photo;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "days")
	private Integer days;
	
	@Column(name = "price")
	private Integer price;
	
//	@Column(name = "start_time")
//	private Date startTime;
//	
//	@Column(name = "end_time")
//	private Date endTime;
	@Column(name = "start_time")
	private LocalDate startTime;
	
	@Column(name = "end_time")
	private LocalDate endTime;
	
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ad_id")
	private AdCheck adCheck;
	

}
