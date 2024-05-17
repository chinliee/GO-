package com.team4.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.reply.ReplyCreateDto;
import com.team4.demo.model.dto.reply.ReplyUpdateDto;
import com.team4.demo.model.entity.Reply;
import com.team4.demo.service.ReplyService;

@RestController
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	
	@GetMapping("/replys")
	public ResponseEntity<List<Reply>> getAllreplys(){
		
		List<Reply> replys = replyService.getAllreplys();
		
		return new ResponseEntity<>(replys,HttpStatus.OK);
	}
	
	@PostMapping("/replys/create")
	public ResponseEntity<?> createReply(@RequestBody ReplyCreateDto createDto){

		 replyService.createReply(createDto);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("replys/delete/{reply_id}")
	public void deleteReply(@PathVariable("reply_id") Integer reply_id) {
		replyService.deleteReply(reply_id);
	}
	
	@PutMapping("replys/update/{reply_id}")
	public void updateReply(@PathVariable("reply_id") Integer reply_id, @RequestBody ReplyUpdateDto updateDto) {
		replyService.updateReply(reply_id, updateDto);
	}
	@PutMapping("/replys/emailreply/{reply_id}")
	public void mailreply(@PathVariable("reply_id") Integer reply_id,@RequestParam("contenttext") String contenttext) {
		replyService.emailreply(reply_id, contenttext);
	}
}
