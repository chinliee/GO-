package com.team4.demo.model.dto.reply;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyUpdateDto {

    private Integer replyId;


    private Integer contactadminId;


    private String email;


    private String contenttext;

    private Date replyTime;

	    
}
