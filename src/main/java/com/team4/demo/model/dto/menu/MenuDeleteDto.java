package com.team4.demo.model.dto.menu;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDeleteDto {

    private Integer restaurantId;

    private List<Integer> menuIds;

}
