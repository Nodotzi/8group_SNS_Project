package com.sparta.snsproject.repository;

import com.sparta.snsproject.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {

//    List<Posting> findAllByUserId(Long user);
}
