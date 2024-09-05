package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.friends.FriendsDeleteRequestDto;
import com.sparta.snsproject.dto.relationship.RelationshipAcceptRequestDto;
import com.sparta.snsproject.dto.relationship.RelationshipResponseDto;
import com.sparta.snsproject.dto.relationship.RelationshipSendRequestDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.dto.user.UserSimpleResponseDto;
import com.sparta.snsproject.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//친구 요청, 수락, 삭제와 관련된 페이지
@RestController
@RequestMapping("/api/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final RelationshipService relationshipService;

    /***
     * 친구 요청
     * @param signUser : 나의 로그인 정보
     * @param requestDto : 친구 요청받은 유저의 아이디 정보
     * @return relationship 등록 정보
     */
    @PostMapping("/send")
    public ResponseEntity<RelationshipResponseDto> sendFriend(@Sign SignUser signUser, @RequestBody RelationshipSendRequestDto requestDto) {
        return ResponseEntity.ok(relationshipService.sendFriend(signUser, requestDto));
    }

    /***
     * 친구 수락
     * @param signUser : 나의 로그인 정보
     * @param requestDto : 요청한 유저 아이디 정보
     * @return relationship 등록 정보
     */
    @PutMapping("/accept")
    public ResponseEntity<RelationshipResponseDto> acceptFriend(@Sign SignUser signUser, @RequestBody RelationshipAcceptRequestDto requestDto) {
        return ResponseEntity.ok(relationshipService.acceptFriend(signUser, requestDto));
    }

    /***
     * 친구 삭제
     * @param signUser : 나의 로그인 정보
     * @param requestDto : 삭제할 친구 아이디 정보
     */
    @DeleteMapping("/remove")
    public ResponseEntity deletedFriend(@Sign SignUser signUser, @RequestBody FriendsDeleteRequestDto requestDto) {
        relationshipService.deletedFriend(signUser, requestDto);
        return ResponseEntity.noContent().build();  //204번 : body에 아무것도 넣지 못함
    }
    /***
     * 요청받은 입장에서 요청한 유저들 목록
     * @param signUser : 나의 로그인 정보
     * @return  요청한 유저 목록
     */

    @GetMapping("/sendFriendlist")
    public ResponseEntity<List<UserSimpleResponseDto>> sendFriendList(@Sign SignUser signUser) {
        return ResponseEntity.ok(relationshipService.sendFriendList(signUser));
    }


    /**
     * 내가 요청한 친구 목록
     * @param signUser : 나의 로그인 정보
     * @return 친구 오쳥 목록
     */
    @GetMapping("/receiveFriendlist")
    public ResponseEntity<List<UserSimpleResponseDto>> receiveFriendList(@Sign SignUser signUser) {
        return ResponseEntity.ok(relationshipService.receiveFriendList(signUser));
    }

    /**
     *친구 요청 취소
     * @param signUser : 나의 로그인 정보
     * @param requestDto  : 친구 요청받은 유저의 아이디 정보
     */
    @DeleteMapping("/cancel")
    public ResponseEntity cancelSend(@Sign SignUser signUser, @RequestBody RelationshipSendRequestDto requestDto) {
        relationshipService.cancelSend(signUser, requestDto);
        return ResponseEntity.noContent().build(); //204번 : body에 아무것도 넣지 못함
    }

    /**
     *친구 목록 조회
     * @param signUser : 나의 로그인 정보
     * @return : 친구 목록
     */
    @GetMapping("/getfriends")
    public ResponseEntity<List<UserSimpleResponseDto>> getFriends(@Sign SignUser signUser) {
        return ResponseEntity.ok(relationshipService.getfriends(signUser));
    }
}
