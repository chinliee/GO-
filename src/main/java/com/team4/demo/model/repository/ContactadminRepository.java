package com.team4.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.Contactadmin;
@Repository
public interface ContactadminRepository extends JpaRepository<Contactadmin, Integer> {

}
