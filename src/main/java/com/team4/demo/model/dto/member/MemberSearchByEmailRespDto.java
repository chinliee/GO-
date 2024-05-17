package com.team4.demo.model.dto.member;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchByEmailRespDto {

    private Integer memberId;

    private String email;

}
