package com.team4.demo.model.dto.member;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberGetAllMenuCartRespDto {

    // private Integer memberId;

    private String email;

    private String password;

    private String salt;

    private String name;

    private String phone;

    private String county;

    private String district;

    private String address;

    private String status;

    private byte[] photo;

    private Date registeration_date;

    private String bin;

    private String pan;

    private String luhn;

    private String existence;

}
