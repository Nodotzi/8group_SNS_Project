package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.entity.AskStatus;
import com.sparta.snsproject.entity.Friends;
import com.sparta.snsproject.entity.Relationship;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.repository.FriendsRepository;
import com.sparta.snsproject.repository.RelationshipRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    //친구 요청
    @Transactional
    public RelationshipResponseDto sendFriend(SignUser signUser, RelationshipSendRequestDto requestDto) {
        //요청한 유저 찾기
        User send = userRepository.findById(signUser.getId()).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        //요청 받은 유저 찾기
        User receive = userRepository.findById(requestDto.getReceive_id()).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        //두 유저로 relationship객체 생성(상태는 자동으로 wait)
        Relationship relationship = new Relationship(send, receive);
        //데이터 저장
        Relationship saveRelationship = relationshipRepository.save(relationship);
        //저장된 relationship 데이터를 responseDto로 보냄
        return new RelationshipResponseDto(saveRelationship);

    }

    @Transactional
    public RelationshipResponseDto acceptFriend(SignUser signUser, RelationshipAcceptRequestDto requestDto) {
        //요청한 유저와 요청받은 유저의 아이디로 해당 relationship 찾기
        Relationship relationship = relationshipRepository.findBySendIdAndReceiveId(requestDto.getSend_id(), signUser.getId()).orElseThrow(()->new NullPointerException("친구 요청한 기록이 없습니다"));
        //relatiohship 상태를 accept로 변경
        relationship.accept();
        //수정된 데이터 저장
        Relationship saveRelationship = relationshipRepository.save(relationship);
        //두 아이디로 두 유저 찾기
        User send = userRepository.findById(requestDto.getSend_id()).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        User receive = userRepository.findById(signUser.getId()).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        //두 유저로 친구 객체 생성
        Friends friends = new Friends(send, receive);
        //친구 데이터 저장
        friendsRepository.save(friends);
        //두 유저의 자리를 바꿔서 다시 객체 생성 후 데이터 저장
        friends = new Friends(receive, send);
        friendsRepository.save(friends);
        //수정된 relationship의 정보를 responseDto로 보내기
        return new RelationshipResponseDto(saveRelationship);
    }

    //친구 삭제
    @Transactional
    public void deletedFriend(SignUser signUser, FriendsDeleteRequestDto requestDto) {
        Long friendAId = signUser.getId();
        Long friendBId = requestDto.getFriendId();
        //둘중 누가 요청자인지 모르므로 둘다 번갈아 넣어서 해당 relationship 데이터 찾기
        Relationship relationship = relationshipRepository.findBySendIdAndReceiveId(friendAId, friendBId)
                .orElseGet(()->relationshipRepository.findBySendIdAndReceiveId(friendBId,friendAId)
                .orElseThrow(()-> new NullPointerException("친구 요청한 기록이 없습니다")));
        //relationship의 해당 데이터 삭제
        relationshipRepository.delete(relationship);
        //두 아이디로 생성된 친구 데이터를 찾아 삭제(2개 생성했으므로 2개 다 삭제)
        Friends friends = friendsRepository.findByFriendAIdAndFriendBId(friendAId, friendBId).orElseThrow(()->new NullPointerException("해당하는 친구가 존재하지 않습니다"));
        friendsRepository.delete(friends);
        friends =friendsRepository.findByFriendAIdAndFriendBId(friendBId, friendAId).orElseThrow(()->new NullPointerException("해당하는 친구가 존재하지 않습니다"));
        friendsRepository.delete(friends);
    }

    //요청받은 사람이 보는 요청한 유저 목록
    public List<UserSimpleResponseDto> sendFriendList(SignUser signUser) {
        //요청 받은 사람이 askedId인 relationship 목록
        List<Relationship> askingList = relationshipRepository.findAllByReceiveIdAndStatus(signUser.getId(), AskStatus.WAIT);
        //리스트 속 각 relationship를 요청한 유저를 추출하고 UserSimpleResponseDto로 변환 -> 내보내기
        return askingList.stream()
                .map(Relationship::getSend)
                .map(UserSimpleResponseDto::new).toList();
    }

    //내가 요청한 친구 목록
    public List<UserSimpleResponseDto> receiveFriendList(SignUser signUser) {
        //relationship 데이터에서 asking_id가 askingId인 데이터 목록 찾기
        List<Relationship> askedList = relationshipRepository.findAllBySendIdAndStatus(signUser.getId(), AskStatus.WAIT);

        //리스트 속 각 relationship를 요청받은 유저를 추출하고 UserSimpleResponseDto로 변환 -> 내보내기
        return askedList.stream()
                .map(Relationship::getReceive)
                .map(UserSimpleResponseDto::new).toList();
    }

    //친구 요청 취소
    @Transactional
    public void cancelSend(SignUser signUser, RelationshipSendRequestDto requestDto) {
        //나와 친구 신청된 유저의 아이디로 해당 relationship 객체 찾기
        Relationship relationship = relationshipRepository.findBySendIdAndReceiveId(signUser.getId(), requestDto.getReceive_id()).orElseThrow(()->new NullPointerException("친구신청한 기록이 없습니다."));
        //데이터 삭제
        relationshipRepository.delete(relationship);
    }

    //친구 목록 조홰
    public List<UserSimpleResponseDto> getfriends(SignUser signUser) {
        List<Friends> friendsList = friendsRepository.findAllByFriendAId(signUser.getId());
        return friendsList.stream()
                .map(Friends::getFriendB)
                .map(UserSimpleResponseDto::new)
                .toList();
    }
}
