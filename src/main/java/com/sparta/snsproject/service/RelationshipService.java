package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.relationship.RelationshipResponseDto;
import com.sparta.snsproject.entity.Friends;
import com.sparta.snsproject.entity.Relationship;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.repository.FriendsRepository;
import com.sparta.snsproject.repository.RelationshipRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    //친구 요청
    @Transactional
    public RelationshipResponseDto askingFriend(Long askingId, Long askedId) {
        //요청한 유저 찾기
        User asking = userRepository.findById(askingId).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        //요청 받은 유저 찾기
        User asked = userRepository.findById(askedId).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        //두 유저로 relationship객체 생성(상태는 자동으로 wait)
        Relationship relationship = new Relationship(asking, asked);
        //데이터 저장
        Relationship saveRelationship = relationshipRepository.save(relationship);
        //저장된 relationship 데이터를 responseDto로 보냄
        return new RelationshipResponseDto(saveRelationship);

    }

    @Transactional
    public RelationshipResponseDto acceptFriend(Long askingId, Long askedId) {
        //요청한 유저와 요청받은 유저의 아이디로 해당 relationship 찾기
        Relationship relationship = relationshipRepository.findByAskingIdAndAskedId(askingId, askedId).orElseThrow(()->new NullPointerException("친구 요청한 기록이 없습니다"));
        //relatiohship 상태를 accept로 변경
        relationship.accept();
        //수정된 데이터 저장
        Relationship saveRelationship = relationshipRepository.save(relationship);
        //두 아이디로 두 유저 찾기
        User asking = userRepository.findById(askingId).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        User asked = userRepository.findById(askedId).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        //두 유저로 친구 객체 생성
        Friends friends = new Friends(asking, asked);
        //친구 데이터 저장
        friendsRepository.save(friends);
        //두 유저의 자리를 바꿔서 다시 객체 생성 후 데이터 저장
        friends = new Friends(asked, asking);
        friendsRepository.save(friends);
        //수정된 relationship의 정보를 responseDto로 보내기
        return new RelationshipResponseDto(saveRelationship);
    }

    //친구 삭제
    @Transactional
    public RelationshipResponseDto deletedFriend(Long friendAId, Long friendBId) {
        //둘중 누가 요청자인지 모르므로 둘다 번갈아 넣어서 해당 relationship 데이터 찾기
        Relationship relationship = relationshipRepository.findByAskingIdAndAskedId(friendAId, friendBId)
                .orElseGet(()->relationshipRepository.findByAskingIdAndAskedId(friendBId,friendAId)
                .orElseThrow(()-> new NullPointerException("친구 요청한 기록이 없습니다")));
        //relationship의 status를 deleted로 변경
        relationship.delete();
        //수정된 데이터 저장
        Relationship saveRelationship = relationshipRepository.save(relationship);
        //두 아이디로 생성된 친구 데이터를 찾아 삭제(2개 생성했으므로 2개 다 삭제)
        Friends friends = friendsRepository.findByFriendAIdAndFriendBId(friendAId, friendBId).orElseThrow(()->new NullPointerException("해당하는 친구가 존재하지 않습니다"));
        friendsRepository.delete(friends);
        friends =friendsRepository.findByFriendAIdAndFriendBId(friendBId, friendAId).orElseThrow(()->new NullPointerException("해당하는 친구가 존재하지 않습니다"));
        friendsRepository.delete(friends);
        return new RelationshipResponseDto(saveRelationship);
    }
}
