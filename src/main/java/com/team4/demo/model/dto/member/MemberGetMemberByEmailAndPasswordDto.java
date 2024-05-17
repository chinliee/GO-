package com.team4.demo.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberGetMemberByEmailAndPasswordDto {

    private String email;

    private String password;

}
