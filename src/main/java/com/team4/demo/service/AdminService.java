package com.team4.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.team4.demo.model.dto.admin.AdminCreateDto;
import com.team4.demo.model.dto.admin.AdminGetAdminByEmailRespDto;
import com.team4.demo.model.dto.admin.AdminGetAllAdminRespDto;
import com.team4.demo.model.dto.admin.AdminUpdateDto;
import com.team4.demo.model.entity.Admin;
import com.team4.demo.model.repository.AdminRepository;
import com.team4.demo.util.SaltGenerator;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<AdminGetAdminByEmailRespDto> getAdminByEmail(String email) {
        List<Admin> adminList = adminRepository.findAdminByEmail(email);
        List<AdminGetAdminByEmailRespDto> respDtoList = adminList.stream()
            .map(admin -> {
                AdminGetAdminByEmailRespDto respDto = new AdminGetAdminByEmailRespDto();
                BeanUtils.copyProperties(admin, respDto);
                return respDto;
            })
            .collect(Collectors.toList());

        return respDtoList;
    }

    public List<AdminGetAllAdminRespDto> getAllAdmin() {
        List<Admin> adminList = adminRepository.findAll();
        List<AdminGetAllAdminRespDto> respDtoList = adminList.stream()
            .map(admin -> {
                AdminGetAllAdminRespDto respDto = new AdminGetAllAdminRespDto();
                BeanUtils.copyProperties(admin, respDto);
                return respDto;
            })
            .collect(Collectors.toList());

        return respDtoList;
    }

    public void deleteAdminById(Integer adminId) {
        adminRepository.findById(adminId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 adminId => " + adminId + " 不存在"));
        adminRepository.deleteById(adminId);
    }

    public void createAdmin(AdminCreateDto createDto) {
        Admin admin = new Admin();
        admin.setEmail(createDto.getEmail());
        admin.setPassword(createDto.getPassword());
        admin.setName(createDto.getName());
        admin.setStatus(createDto.getStatus());
        adminRepository.save(admin);
    }

    public void updateAdmin(Integer adminId, AdminUpdateDto updateDto) {
        Admin admin = adminRepository.findById(adminId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 adminId => " + adminId + " 不存在"));
        
        admin.setEmail(updateDto.getEmail());
        admin.setPassword(updateDto.getPassword());
        admin.setName(updateDto.getName());
        admin.setStatus(updateDto.getStatus());
        adminRepository.save(admin);
    }

    public List<String> authenticateAndGetAdminId(String username, String password) {
        Admin admin = adminRepository.findByEmail(username);
        System.out.println(admin);
        if (admin == null) {
            return null; // 或者你可以拋出一個異常或者返回一個錯誤消息
        }
        
        String storedPassword = admin.getPassword();
        String storedSalt = admin.getSalt();
        System.out.println(storedPassword);
        System.out.println(storedSalt);
        String hashedPassword = SaltGenerator.hashPassword(password, storedSalt);
        
        // 比較資料庫中儲存的密碼和輸入密碼的雜湊值
        if (hashedPassword.equals(storedPassword)) {
            ArrayList<String> adminBack = new ArrayList<>();
            // 如果驗證成功，返回餐廳 ID
            String adminId = Integer.toString(admin.getAdminId());
            String adminName = admin.getName();
            
            adminBack.add(adminId);
            adminBack.add(adminName);
            return adminBack;
        } else {
            return null; // 或者你可以拋出一個異常或者返回一個錯誤消息
        }
    }

}
