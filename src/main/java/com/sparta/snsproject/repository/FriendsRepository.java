package com.sparta.snsproject.repository;

import com.sparta.snsproject.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    Optional<Friends> findByFriendAIdAndFriendBId(Long friendAId, Long friendBId);
}
