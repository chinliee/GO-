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

import com.team4.demo.model.dto.contact.ContactCreateDto;
import com.team4.demo.model.dto.contact.ContactUpdateDto;
import com.team4.demo.model.entity.Contactadmin;
import com.team4.demo.service.ContactadminService;


@RestController
public class ContactController {

	@Autowired
	private ContactadminService contactadminService;
	
	
	@GetMapping("/contacts")
	public ResponseEntity<List<Contactadmin>> getAllContacts(){
		
		List<Contactadmin> contacts = contactadminService.getAllcontacts();
		
		return new ResponseEntity<>(contacts,HttpStatus.OK);
	}
	
	@PostMapping("/contacts/create")
	public ResponseEntity<?> createContact(@RequestBody ContactCreateDto createDto ) {
		System.out.println(createDto);
		contactadminService.createContact(createDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@DeleteMapping("contacts/delete/{contactadmin_id}")
	public void deleteContact(@PathVariable("contactadmin_id") Integer contactadmin_id) {
		contactadminService.deleteContact(contactadmin_id);
	}
	
	@PutMapping("contacts/update/{contactadmin_id}")
	public void updateContact(@PathVariable("contactadmin_id") Integer contactadmin_id, @RequestBody ContactUpdateDto updateDto) {
		contactadminService.updateContact(contactadmin_id, updateDto);
	}
	
	@PutMapping("/contacts/emailreply/{contactadmin_id}")
	public void mailreply(@PathVariable("contactadmin_id") Integer contactadmin_id,@RequestBody String contenttext) {
		contactadminService.emailreply(contactadmin_id, contenttext);
	}
	
}
