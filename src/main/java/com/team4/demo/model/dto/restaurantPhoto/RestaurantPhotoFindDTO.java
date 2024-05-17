package com.team4.demo.model.dto.restaurantPhoto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantPhotoFindDTO {

	  private byte[] photoFile;
	  	  
	  private Integer restaurantId;
	
	  private String name;
	  
	  private String style;
		
}
