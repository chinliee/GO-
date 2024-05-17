package com.team4.demo.model.dto.member;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberGetAllMemberRespDto {

    private Integer memberId;

    private String email;

    private String name;

    private String phone;

    private String address;

    private byte[] photo;

    private Date registeration_date;

}
