package com.sparta.snsproject.controller;

import com.sparta.snsproject.dto.relationship.RelationshipResponseDto;
import com.sparta.snsproject.dto.user.UserSimpleResponseDto;
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
     * @param asking_id : 친구 요청한 유저 아이디
     * @param asked_id : 요청받은 유저 아이디
     * @return relationship 등록 정보
     */
    @PostMapping("/{asking_id}/{asked_id}")
    public RelationshipResponseDto askingFriend(@PathVariable("asking_id") Long asking_id, @PathVariable("asked_id") Long asked_id) {
        return relationshipService.askingFriend(asking_id, asked_id);
    }

    /***
     * 친구 수락
     * @param asking_id : 친구 요청한 유저 아이디
     * @param asked_id : 요청받은 유저 아이디
     * @return relationship 등록 정보
     */
    @PutMapping("/{asking_id}/{asked_id}")
    public RelationshipResponseDto acceptFriend(@PathVariable("asking_id") Long asking_id, @PathVariable Long asked_id) {
        return relationshipService.acceptFriend(asking_id, asked_id);
    }

    /***
     * 친구 삭제
     * @param friendA_id : 삭제 요청한 유저 아이디
     * @param friendB_id : 요청받은 유저 아이디
     * @return relationship 등록 정보
     */
    @DeleteMapping("/{friendA_id}/{friendB_id}")
    public RelationshipResponseDto deletedFriend(@PathVariable("friendA_id") Long friendA_id, @PathVariable("friendB_id") Long friendB_id) {
        return relationshipService.deletedFriend(friendA_id, friendB_id);
    }
    /***
     * 요청받은 입장에서 요청한 유저들 목록
     * @param asked_id : 요청받은 유정 아이디
     * @return  요청한 유저 목록
     */

    @GetMapping("/asked/{asked_id}")
    public List<UserSimpleResponseDto> askedFriendList(@PathVariable("asked_id") Long asked_id) {
        return relationshipService.askedFriendList(asked_id);
    }


    /**
     * 내가 요청한 친구 목록
     * @param asking_id : 내 유저 아이디
     * @return 친구 오쳥 목록
     */
    @GetMapping("/asking/{asking_id}")
    public List<UserSimpleResponseDto> askingFriendList(@PathVariable("asking_id") Long asking_id) {
        return relationshipService.askingFriendList(asking_id);
    }
}
