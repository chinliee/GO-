package com.team4.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "ad_check")
public class AdCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ad_check_id")
	private Integer adCheckId;
	
	@Column(name = "ad_id")
	private Integer adId;
	
	@Column(name = "check_count")
	private Integer checkCount;


}
