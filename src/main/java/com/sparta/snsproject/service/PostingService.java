package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.posting.NewsfeedResponseDto;
import com.sparta.snsproject.dto.posting.PostingRequestDto;
import com.sparta.snsproject.dto.posting.PostingResponseDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.entity.Friends;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.entity.UserStatusEnum;
import com.sparta.snsproject.exception.NoSignedUserException;

import com.sparta.snsproject.repository.FriendsRepository;
import com.sparta.snsproject.repository.PostingRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CommonService commonService;

    //새 게시물을 생성하고 저장
    @Transactional
    public PostingResponseDto savePosting(SignUser signUser, PostingRequestDto postingRequestDto) {
        User user = commonService.findUser(signUser.getId());
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
        Posting posting = commonService.findPosting(posting_id);
        return new PostingResponseDto(
                posting.getId(),
                posting.getTitle(),
                posting.getContents(),
                posting.getUser());
    }

    //게시물을 업데이트
    @Transactional
    public PostingResponseDto updatePosting(Long posting_id, PostingRequestDto postingRequestDto, SignUser signUser) {
        Posting posting = commonService.findPosting(posting_id);

        // 게시물 작성자와 현재 사용자가 같은지 확인
        commonService.confirmCreator(posting.getUser().getId(), signUser.getId(), false);

        posting.update(postingRequestDto.getContents(), postingRequestDto.getTitle());
        postingRepository.save(posting);
        return new PostingResponseDto(posting.getId(), posting.getTitle(), posting.getContents(),
                posting.getUser()
        );
    }

    //게시물 ID로 게시물을 삭제
        public void deletePosting(Long posting_id, SignUser signUser) {
            Posting posting = commonService.findPosting(posting_id);

            // 게시물 작성자와 현재 사용자가 같은지 확인
            commonService.confirmCreator(posting.getUser().getId(), signUser.getId(), true);
            // 삭제 작업
            postingRepository.deleteById(posting_id);
        }

    public Page<NewsfeedResponseDto> getNewsfeed(Long userId, int pageNumber) {
        //받아온 id에맞는 유저찾기
        User user = commonService.findUser(userId);

        if(user.getUser_status().equals(UserStatusEnum.DISABLE)) throw new NoSignedUserException();
        //관계테이블에서 친구관계인 유저들 다 찾기
        List<Friends> friendsList = friendsRepository.findAllByFriendAId(user.getId());

        //찾은 유저들 리스트에 저장
        List<User> userlist = new ArrayList<>();
        for(Friends f:friendsList) {
            userlist.add(f.getFriendB());
        }

        //페이지네이션
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());
        Page<Posting> pages = postingRepository.findAllByUserIn(userlist, pageable);
        return pages.map(NewsfeedResponseDto::new);
    }
}