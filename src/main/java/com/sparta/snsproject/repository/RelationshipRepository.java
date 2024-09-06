package com.sparta.snsproject.repository;


import com.sparta.snsproject.entity.AskStatus;
import com.sparta.snsproject.entity.Relationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Optional<Relationship> findBySendIdAndReceiveId(Long sendId, Long receiveId);

    Page<Relationship> findAllBySendIdAndStatus(Long sendId, AskStatus status, Pageable pageable);

    Page<Relationship> findAllByReceiveIdAndStatus(Long receiveId, AskStatus status, Pageable pageable);

    List<Relationship> findAllBySendId(Long sendid);
}
