package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.repository.PostingRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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


    @Transactional
    public PostingResponseDto savePosting(SignUser signUser, PostingRequestDto postingRequestDto) {
        User user = userRepository.findById(signUser.getId()).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
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
}





