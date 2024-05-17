package com.team4.demo.model.dto.contact;

import java.util.Date;


import lombok.Data;


@Data
public class ContactUpdateDto {

	private String name;

	private String email;

	private String contenttext;

	private String status;

	private Date createdTime;

}
