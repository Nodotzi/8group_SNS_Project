package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.NewsfeedResponseDto;
import com.sparta.snsproject.dto.PostingRequestDto;
import com.sparta.snsproject.dto.PostingResponseDto;
import com.sparta.snsproject.dto.SignUser;
import com.sparta.snsproject.entity.Friends;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
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

    public List<PostingResponseDto> getPostings(SignUser signUser) {
        List<Posting> postingList = postingRepository.findAllByUserId(signUser.getId());

        List<PostingResponseDto> dtoList = new ArrayList<>();

        for (Posting posting : postingList) {
            PostingResponseDto dto = new PostingResponseDto(
                    posting.getId(), posting.getTitle(), posting.getContents(), posting.getUser());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public PostingResponseDto getPosting(Long posting_id) {
        Posting posting = postingRepository.findById(posting_id).orElseThrow(() -> new NullPointerException("없음"));
        return new PostingResponseDto(
                posting.getId(),
                posting.getTitle(),
                posting.getContents(),
                posting.getUser());
    }



    @Transactional
    public PostingResponseDto updatePosting(Long posting_id, PostingRequestDto postingRequestDto) {
        Posting posting = postingRepository.findById(posting_id).orElseThrow(() -> new NullPointerException("없음"));
        posting.update(postingRequestDto.getContents(), postingRequestDto.getTitle());
        postingRepository.save(posting);
        return new PostingResponseDto(posting.getId(), posting.getTitle(), posting.getContents(),
                posting.getUser()
        );
    }

    @Transactional
    public void deletePosting(Long posting_id){
        postingRepository.deleteById(posting_id);
    }

    public Page<NewsfeedResponseDto> getNewsfeed(Long id, int pageNumber) {
        User user = userRepository.findById(id).orElseThrow();
        //중간테이블에서 친구관계인 유저들 다 찾기
        List<Friends> friendsList = friendsRepository.findAllByFriendAId(user.getId());
        //찾은 유저들 리스트에 저장
        List<User> userlist = new ArrayList<>();
        for(Friends f:friendsList) {
            userlist.add(f.getFriendB());
        }
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());
        Page<Posting> pages = postingRepository.findAllByUserIn(userlist, pageable);
        return pages.map(NewsfeedResponseDto::new);
    }
}