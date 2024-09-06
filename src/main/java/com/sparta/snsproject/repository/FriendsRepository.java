package com.sparta.snsproject.repository;

import com.sparta.snsproject.entity.Friends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    Optional<Friends> findByFriendAIdAndFriendBId(Long friendAId, Long friendBId);
    List<Friends> findAllByFriendAId(Long id);
    Page<Friends> findAllByFriendAId(Long id, Pageable pageable);

    List<Friends> findAllByFriendBId(Long id);
}
