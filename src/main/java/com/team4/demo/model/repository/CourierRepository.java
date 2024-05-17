package com.team4.demo.model.repository;

import com.team4.demo.model.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Integer> {

    List<Courier> findByStatusAndOnlineStatus(Integer status, Integer onlineStatus);

}
