package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.friends.FriendsDeleteRequestDto;
import com.sparta.snsproject.dto.relationship.*;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.dto.user.UserSimpleResponseDto;
import com.sparta.snsproject.entity.*;
import com.sparta.snsproject.exception.ExistRelationshipException;
import com.sparta.snsproject.exception.NotFoundException;
import com.sparta.snsproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final PostingRepository postingRepository;


    //친구 요청
    @Transactional
    public RelationshipResponseDto sendFriend(SignUser signUser, RelationshipSendRequestDto requestDto) {
        //요청한 유저 찾기
        User send = findUser(signUser.getId());
        //요청 받은 유저 찾기
        User receive = findUser(requestDto.getReceive_id());
        //이미 털톼한 유저에게는 요청을 보낼 수 없음
        if(receive.getUser_status() == UserStatusEnum.DISABLE) throw new IllegalArgumentException("이미 탈퇴한 유저입니다.");
        //친구 요청을 나에게 할 수 없음
        if(Objects.equals(signUser.getId(), requestDto.getReceive_id())) throw new IllegalArgumentException("자신에게는 친구요청을 할 수 없습니다.");
        //이미 신청받아있다면 거부하고 수락해달라는 메세지 날림
        Optional<Relationship> alreadyReceiveRelationship = relationshipRepository.findBySendIdAndReceiveId(requestDto.getReceive_id(), signUser.getId());
        if (alreadyReceiveRelationship.isPresent()) {
            throw new ExistRelationshipException("이미 친구 요청받은 유저입니다. 수락해주세요.");
        }
        //이미 신청했다면 신청되었다고 메시지 날림
        Optional<Relationship> alreadySendRelationship = relationshipRepository.findBySendIdAndReceiveId(signUser.getId(), requestDto.getReceive_id());
        if (alreadySendRelationship.isPresent()) {
            throw new ExistRelationshipException("이미 친구 요청한 유저입니다.");
        }
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
        Relationship relationship = findRelationship(requestDto.getSend_id(), signUser.getId());
        //relatiohship 상태를 accept로 변경
        relationship.accept();
        //수정된 데이터 저장
        Relationship saveRelationship = relationshipRepository.save(relationship);
        //두 아이디로 두 유저 찾기
        User send = findUser(requestDto.getSend_id());
        User receive = findUser(signUser.getId());
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
                .orElseThrow(()-> new NotFoundException("친구 요청한 기록이 없습니다")));
        //relationship의 해당 데이터 삭제
        relationshipRepository.delete(relationship);
        //두 아이디로 생성된 친구 데이터를 찾아 삭제(2개 생성했으므로 2개 다 삭제)
        Friends friends = friendsRepository.findByFriendAIdAndFriendBId(friendAId, friendBId).orElseThrow(()->new NotFoundException("해당하는 친구가 존재하지 않습니다"));
        friendsRepository.delete(friends);
        friends =friendsRepository.findByFriendAIdAndFriendBId(friendBId, friendAId).orElseThrow(()->new NotFoundException("해당하는 친구가 존재하지 않습니다"));
        friendsRepository.delete(friends);
    }

    //요청받은 사람이 보는 요청한 유저 목록
    public Page<UserSimpleResponseDto> sendFriendList(SignUser signUser, int page, int size) {
        //page 속성 넣기
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("createdAt").ascending());
        //요청 받은 사람이 askedId인 relationship 목록
        Page<Relationship> sendList = relationshipRepository.findAllByReceiveIdAndStatus(signUser.getId(), AskStatus.WAIT, pageable);
        //리스트 속 각 relationship를 요청한 유저를 추출하고 UserSimpleResponseDto로 변환 -> 내보내기
        return sendList
                .map(Relationship::getSend)
                .map(UserSimpleResponseDto::new);
    }

    //내가 요청한 친구 목록
    public Page<UserSimpleResponseDto> receiveFriendList(SignUser signUser, int page, int size) {
        //page 속성 넣기
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("createdAt").ascending());
        //relationship 데이터에서 asking_id가 askingId인 데이터 목록 찾기
        Page<Relationship> receiveList = relationshipRepository.findAllBySendIdAndStatus(signUser.getId(), AskStatus.WAIT, pageable);

        //리스트 속 각 relationship를 요청받은 유저를 추출하고 UserSimpleResponseDto로 변환 -> 내보내기
        return receiveList
                .map(Relationship::getReceive)
                .map(UserSimpleResponseDto::new);
    }

    //친구 요청 취소
    @Transactional
    public void cancelSend(SignUser signUser, RelationshipSendRequestDto requestDto) {
        //나와 친구 신청된 유저의 아이디로 해당 relationship 객체 찾기
        Relationship relationship = findRelationship(signUser.getId(), requestDto.getReceive_id());
        //데이터 삭제
        relationshipRepository.delete(relationship);
    }

    //친구 목록 조홰
    public Page<UserSimpleResponseDto> getfriends(SignUser signUser, int page, int size) {
        //page 속성 넣기
        Pageable pageable = PageRequest.of(page-1, size);

        //친구 목록 조회
        Page<Friends> friendsList = friendsRepository.findAllByFriendAId(signUser.getId(), pageable);
        //responseDto형태로 내보내기
        return friendsList
                .map(Friends::getFriendB)
                .map(UserSimpleResponseDto::new);
    }

    //회원 탈퇴시 게시물, 친구 요청 기록, 친구목록 삭제
    @Transactional
    public void signoutUser(Long userId) {
        //user객체가 있는지 확인
        userRepository.findById(userId).orElseThrow(()-> new NotFoundException("해당하는 아이디의 유저가 존재하지 않습니다."));
        //이 유저가 친구 요창한 기록이 있으면 모두 삭제
        List<Relationship> relationships = relationshipRepository.findAllBySendId(userId);
        relationshipRepository.deleteAll(relationships);
        //이 유저와 친구 관계인 목록 다 삭제
        List<Friends> friendsList = friendsRepository.findAllByFriendAId(userId);
        friendsRepository.deleteAll(friendsList);
        friendsList = friendsRepository.findAllByFriendBId(userId);
        friendsRepository.deleteAll(friendsList);
        //이 유저의 게시물 모두 삭제
        List<Posting> postingList = postingRepository.findAllByUserId(userId);
        postingRepository.deleteAll(postingList);
    }

    private Relationship findRelationship(Long sendId, Long receiveId) {
        return relationshipRepository.findBySendIdAndReceiveId(sendId, receiveId).orElseThrow(()->new NotFoundException("친구 요청한 기록이 없습니다"));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new NotFoundException("해당하는 아이디의 유저가 존재하지 않습니다."));
    }
}
