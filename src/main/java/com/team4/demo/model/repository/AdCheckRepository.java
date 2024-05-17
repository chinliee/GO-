package com.team4.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.AdCheck;
@Repository
public interface AdCheckRepository extends JpaRepository<AdCheck, Integer> {

}
