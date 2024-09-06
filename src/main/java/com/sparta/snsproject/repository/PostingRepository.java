package com.sparta.snsproject.repository;

import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
    Page<Posting> findAllByUserIn(List<User> user, Pageable pageable);
    List<Posting> findAllByUserId(Long user);
}
