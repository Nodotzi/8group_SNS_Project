package com.sparta.snsproject.repository;


import com.sparta.snsproject.entity.AskStatus;
import com.sparta.snsproject.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Optional<Relationship> findByAskingIdAndAskedId(Long askingId, Long askedId);

    List<Relationship> findAllByAskedIdAndStatus(Long askedId, AskStatus status);

    List<Relationship> findAllByAskingIdAndStatus(Long askingId, AskStatus status);
}
