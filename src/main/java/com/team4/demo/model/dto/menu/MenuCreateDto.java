package com.team4.demo.model.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreateDto {

    private Integer restaurantId;

    private String name;

    private Integer price;

    private String description;

    private MultipartFile photo;

}
