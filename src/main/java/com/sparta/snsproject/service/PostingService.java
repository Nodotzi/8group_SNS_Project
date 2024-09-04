package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.entity.Friends;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.repository.FriendsRepository;
import com.sparta.snsproject.repository.PostingRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostingService {

    private final PostingRepository postingRepository;
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;


    @Transactional
    public PostingSaveResponseDto savePosting(PostingSaveRequestDto postingSaveRequestDto) {
        User user = userRepository.findById(postingSaveRequestDto.getUserId()).orElseThrow();
        Posting newPosting = new Posting(
                postingSaveRequestDto.getTitle(),
                postingSaveRequestDto.getContents(),
                user
        );

        Posting savedPosting = postingRepository.save(newPosting);

        return new PostingSaveResponseDto(
                savedPosting.getId(),
                savedPosting.getTitle(),
                savedPosting.getContents(),
                user
        );
    }

    public List<PostingSimpleResponseDto> getPostings() {
        List<Posting> postingList = postingRepository.findAll();

        List<PostingSimpleResponseDto> dtoList = new ArrayList<>();

        for (Posting posting : postingList) {
            PostingSimpleResponseDto dto = new PostingSimpleResponseDto(
                    posting.getId(), posting.getTitle(), posting.getContents(), posting.getUser());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public PostingDetaliResponseDto getPosting(Long posting_id) {
        Posting posting = postingRepository.findById(posting_id).orElseThrow(() -> new NullPointerException("없음"));
        return new PostingDetaliResponseDto(
                posting.getId(),
                posting.getTitle(),
                posting.getContents(),
                posting.getUser());
    }



    @Transactional
    public PostingUpdateResponseDto updatePosting(Long posting_id, PostingUpdateRequestDto postingUpdateRequestDto) {
        Posting posting = postingRepository.findById(posting_id).orElseThrow(() -> new NullPointerException("없음"));
        posting.update(posting.getContents(), posting.getTitle());

        return new PostingUpdateResponseDto(posting.getId(), posting.getTitle(), posting.getContents(),
                posting.getUser()
        );
    }

    @Transactional
    public void deletePosting(Long posting_id){
        postingRepository.deleteById(posting_id);
    }

    public Page<NewsfeedResponseDto> getNewsfeed(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        //중간테이블에서 친구관계인 유저들 다 찾기
        List<Friends> friendsList = friendsRepository.findAllByFriendAId(user.getId());

        //찾은 유저들 리스트에 저장
        List<User> userlist = new ArrayList<>();
        for(Friends f:friendsList) {
            userlist.add(f.getFriendB());
        }

        //찾은 각각 유저들의 포스팅을 리스트 한곳에 싹다 모으기
        List<Posting> postingList = postingRepository.findAllByUserInOrderByCreatedAtDesc(userlist);

        //리스트 -> 페이지네이션
        PageRequest pageRequest = PageRequest.of(0, 10);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), postingList.size());
        Page<NewsfeedResponseDto> pages = new PageImpl<>(postingList.subList(start, end), pageRequest, postingList.size()).map(NewsfeedResponseDto::new);
        return pages;
    }
}