package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.posting.NewsfeedResponseDto;
import com.sparta.snsproject.dto.posting.PostingRequestDto;
import com.sparta.snsproject.dto.posting.PostingResponseDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.entity.Friends;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.exception.NoSignedUserException;
import com.sparta.snsproject.exception.NotFoundException;
import com.sparta.snsproject.repository.FriendsRepository;
import com.sparta.snsproject.repository.PostingRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    //새 게시물을 생성하고 저장
    @Transactional
    public PostingResponseDto savePosting(SignUser signUser, PostingRequestDto postingRequestDto) {

        User user = userRepository.findById(signUser.getId()).orElseThrow(()-> new NoSignedUserException());
        Posting newPosting = new Posting(
                postingRequestDto.getTitle(),
                postingRequestDto.getContents(),
                user
        );

        Posting savedPosting = postingRepository.save(newPosting);

        return new PostingResponseDto(
                savedPosting.getId(),
                savedPosting.getTitle(),
                savedPosting.getContents(),
                savedPosting.getUser()
        );
    }

    //사용자의 모든 게시물을 조회
    public List<PostingResponseDto> getPostings(Long userId) {
        List<Posting> postingList = postingRepository.findAllByUserId(userId);

        List<PostingResponseDto> dtoList = new ArrayList<>();

        for (Posting posting : postingList) {
            PostingResponseDto dto = new PostingResponseDto(
                    posting.getId(), posting.getTitle(), posting.getContents(), posting.getUser());
            dtoList.add(dto);
        }
        return dtoList;
    }



    //특정 게시물을 조회
    public PostingResponseDto getPosting(Long posting_id) {
        Posting posting = postingRepository.findById(posting_id).orElseThrow(() -> new NotFoundException("게시물이 없습니다."));
        return new PostingResponseDto(
                posting.getId(),
                posting.getTitle(),
                posting.getContents(),
                posting.getUser());
    }

    //게시물을 업데이트
    @Transactional
    public PostingResponseDto updatePosting(Long posting_id, PostingRequestDto postingRequestDto, SignUser signUser) {
        Posting posting = postingRepository.findById(posting_id).orElseThrow(() -> new NotFoundException("게시물이 없습니다."));

        // 게시물 작성자와 현재 사용자가 같은지 확인
        if (posting.getUser().getId() == signUser.getId()) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        posting.update(postingRequestDto.getContents(), postingRequestDto.getTitle());
        postingRepository.save(posting);
        return new PostingResponseDto(posting.getId(), posting.getTitle(), posting.getContents(),
                posting.getUser()
        );
    }

    //게시물 ID로 게시물을 삭제
        public void deletePosting(Long posting_id, SignUser signUser) {
            Posting posting = postingRepository.findById(posting_id)
                    .orElseThrow(() -> new NotFoundException("게시물이 없습니다."));

            // 게시물 작성자와 현재 사용자가 같은지 확인

            if (posting.getUser().getId() == signUser.getId()) {
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
            // 삭제 작업
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