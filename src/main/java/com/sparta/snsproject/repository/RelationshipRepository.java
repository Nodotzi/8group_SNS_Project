package com.sparta.snsproject.repository;


import com.sparta.snsproject.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Optional<Relationship> findByAskingIdAndAskedId(Long askingId, Long askedId);
}
