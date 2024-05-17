package com.team4.demo.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Memberlogin {
	
	private String email;
	
	private String password;
}
