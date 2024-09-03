package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.service.RelationshipService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/asking")
    public RelationshipResponseDto askingFriend(@Sign SignUser signUser, @RequestBody RelationshipAskingRequestDto requestDto) {
        return relationshipService.askingFriend(signUser, requestDto);
    }

    /***
     * 친구 수락
     * @param signUser : 나의 로그인 정보
     * @param requestDto : 요청한 유저 아이디 정보
     * @return relationship 등록 정보
     */
    @PutMapping("/accept")
    public RelationshipResponseDto acceptFriend(@Sign SignUser signUser, @RequestBody RelationshipAcceptRequestDto requestDto) {
        return relationshipService.acceptFriend(signUser, requestDto);
    }

    /***
     * 친구 삭제
     * @param signUser : 나의 로그인 정보
     * @param requestDto : 삭제할 친구 아이디 정보
     * @return relationship 등록 정보
     */
    @DeleteMapping("/remove")
    public RelationshipResponseDto deletedFriend(@Sign SignUser signUser, @RequestBody FriendsDeleteRequestDto requestDto) {
        return relationshipService.deletedFriend(signUser, requestDto);
    }
    /***
     * 요청받은 입장에서 요청한 유저들 목록
     * @param signUser : 나의 로그인 정보
     * @return  요청한 유저 목록
     */

    @GetMapping("/askinglist")
    public List<UserSimpleResponseDto> askingFriendList(@Sign SignUser signUser) {
        return relationshipService.askingFriendList(signUser);
    }


    /**
     * 내가 요청한 친구 목록
     * @param signUser : 나의 로그인 정보
     * @return 친구 오쳥 목록
     */
    @GetMapping("/askedlist")
    public List<UserSimpleResponseDto> askedFriendList(@Sign SignUser signUser) {
        return relationshipService.askedFriendList(signUser);
    }

    /**
     *친구 요청 취소
     * @param signUser : 나의 로그인 정보
     * @param requestDto  : 친구 요청받은 유저의 아이디 정보
     */
    @PutMapping("/cancel")
    public void cancelAsked(@Sign SignUser signUser, @RequestBody RelationshipAskingRequestDto requestDto) {
        relationshipService.cancelAsked(signUser, requestDto);
    }

    /**
     *친구 목록 조회
     * @param signUser : 나의 로그인 정보
     * @return : 친구 목록
     */
    @GetMapping("/getfriends")
    public List<UserSimpleResponseDto> getFriends(@Sign SignUser signUser) {
        return relationshipService.getfriends(signUser);
    }
}
