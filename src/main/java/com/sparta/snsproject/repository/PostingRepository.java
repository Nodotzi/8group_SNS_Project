package com.sparta.snsproject.repository;

import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
    List<Posting> findAllByUserInOrderByCreatedAtDesc(List<User> user);
    List<Posting> findAllByUserId(Long user);
}
