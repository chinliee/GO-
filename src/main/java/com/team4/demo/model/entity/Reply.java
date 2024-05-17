package com.team4.demo.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reply")
public class Reply {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "reply_id")
	    private Integer replyId;

	    @Column(name = "contactadmin_id")
	    private Integer contactadminId;

	    @Column(name = "email")
	    private String email;

	    @Column(name = "contenttext")
	    private String contenttext;
	    
	    @Column(name = "reply_time")
	    private Date replyTime;

	    
}
