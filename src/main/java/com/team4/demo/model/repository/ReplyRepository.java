package com.team4.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.Reply;
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

}
