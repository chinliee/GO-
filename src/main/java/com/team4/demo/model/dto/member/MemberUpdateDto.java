package com.team4.demo.model.dto.member;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {

    private Integer memberId;

    private byte[] photo;
    
    private String email;

    private String name;

    private String phone;

    private String address;

    private Date registeration_date;

}
