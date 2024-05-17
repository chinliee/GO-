package com.team4.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team4.demo.model.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM Admin WHERE email = :email")
    List<Admin> findAdminByEmail(@Param("email") String email);

    Admin findByEmail(String email);

}
