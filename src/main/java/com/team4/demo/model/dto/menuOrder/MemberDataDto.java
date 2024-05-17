package com.team4.demo.model.dto.menuOrder;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDataDto {

    private Integer memberId;

    private String name;

    private String email;

}
