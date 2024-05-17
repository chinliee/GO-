package com.team4.demo.model.dto.member;

import java.sql.Date;

import org.hibernate.internal.build.AllowNonPortable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllowNonPortable
public class MemberRegisterDto {

    // private Integer memberId;

    private String name;
	
    private String email;
	
    private String password;
	
    private String salt;

    private String phone;

    private String county;

    private String district;

    private String address;

    // private String status;

    // private byte[] photo;

    private Date registeration_date;

    // private String bin;

    // private String pan;

    // private String luhn;

    // private String existence;

}
