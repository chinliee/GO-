package com.team4.demo.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adminlogin {
	
	private String email;
	
	private String password;
}
