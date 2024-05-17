package com.team4.demo.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminGetAllAdminRespDto {

    private Integer memberId;

    private String email;

    private String name;

    private String status;

}
