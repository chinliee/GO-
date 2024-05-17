package com.team4.demo.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminGetAdminByEmailRespDto {

    private String email;

    private String password;

    private String salt;

    private String name;

    private String status;

}
