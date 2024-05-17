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
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.admin.AdminCreateDto;
import com.team4.demo.model.dto.admin.AdminGetAdminByEmailRespDto;
import com.team4.demo.model.dto.admin.AdminGetAllAdminRespDto;
import com.team4.demo.model.dto.admin.AdminResponseDto;
import com.team4.demo.model.dto.admin.AdminUpdateDto;
import com.team4.demo.model.dto.admin.Adminlogin;
import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.service.AdminService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class AdminController {

	@Autowired
    private AdminService adminService;

    @GetMapping("/admin/search/{email}")
    public ResponseEntity<ResponseDto> getAdminByEmail(@PathVariable("email") String email) {
        log.info("AdminColtroller - getAdminByEmail ... email => {}", email);
        List<AdminGetAdminByEmailRespDto> respDtoList = adminService.getAdminByEmail(email);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDtoList));
    }
    
    @GetMapping("/admin/search/all-admin")
    public ResponseEntity<ResponseDto> getAllAdmin() {
        log.info("AdminController - getAllAdmin ... without parameter");
        List<AdminGetAllAdminRespDto> respDtoList = adminService.getAllAdmin();
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDtoList));
    }

    @DeleteMapping("/admin/delete/{admin_id}")
    public ResponseEntity<ResponseDto> deleteAdminById(@PathVariable("admin_id") Integer adminId) {
        log.info("AdminController - deleteAdminById ... adminId => {}", adminId);
        adminService.deleteAdminById(adminId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    @PostMapping("/admin/create")
    public ResponseEntity<ResponseDto> postAdmin(@RequestBody AdminCreateDto createDto) {
        log.info("AdminController - createAdmin ... createDto => {}", createDto);
        adminService.createAdmin(createDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    @PutMapping("/admin/update/{admin_id}")
    public ResponseEntity<ResponseDto> putAdminById(@PathVariable("admin_id") Integer adminId, @RequestBody AdminUpdateDto updateDto) {
        log.info("AdminController - updateAdminById ... adminId => {}, updateDto => {}", adminId, updateDto);
        adminService.updateAdmin(adminId, updateDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    @PostMapping("/admin/login")
	public  ResponseEntity<?> adLogin(@RequestBody Adminlogin adminlogin) {
	    String username = adminlogin.getEmail();
	    String password = adminlogin.getPassword();
		System.out.println(username);
		System.out.println(password);
	    List<String> isAuthenticated = adminService.authenticateAndGetAdminId(username, password);
        
		System.out.println(password);
	    if (isAuthenticated != null) {
            String adminId = isAuthenticated.get(0);
            String adminName = isAuthenticated.get(1);
            System.out.println(password);
            AdminResponseDto dto = new AdminResponseDto(adminId, adminName);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("郵件與密碼不匹配");
        }
	}



























}
