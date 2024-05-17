package com.team4.demo.model.dto.restaurantSeat;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSeatDto {

private Integer seatId;

private String tableFor;

private String seatState;

private Integer restaurantId;

private LocalDate openDay;

private Integer restaurantRecordId;

private String seatTimePer;

private LocalTime openTime;

}
