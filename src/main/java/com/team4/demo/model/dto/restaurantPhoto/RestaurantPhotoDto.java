package com.team4.demo.model.dto.restaurantPhoto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPhotoDto {

	private Integer restuarntId;

	private byte[] photoFile;
}
