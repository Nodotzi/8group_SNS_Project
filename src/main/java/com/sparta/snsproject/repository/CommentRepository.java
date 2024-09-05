package com.sparta.snsproject.repository;

import com.sparta.snsproject.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByUserId(Long userid, Pageable pageable);

    Page<Comment> findAllByPostingId(Long postingId, Pageable pageable);
}
