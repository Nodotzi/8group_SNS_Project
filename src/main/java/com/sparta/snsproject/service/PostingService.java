package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.repository.PostingRepository;
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


    @Transactional
    public PostingSaveResponseDto savePosting(PostingSaveRequestDto postingSaveRequestDto) {
        Posting newPosting = new Posting(
                postingSaveRequestDto.getTitle(),
                postingSaveRequestDto.getContents(),
                postingSaveRequestDto.getUserId()////보류
        );

        Posting savedPosting = postingRepository.save(newPosting);

        return new PostingSaveResponseDto(
                savedPosting.getId(),
                savedPosting.getTitle(),
                savedPosting.getContents(),
                savedPosting.getUserId()
        );
    }

    public List<PostingSimpleResponseDto> getPostings() {
        List<Posting> postingList = postingRepository.findAll();

        List<PostingSimpleResponseDto> dtoList = new ArrayList<>();

        for (Posting posting : postingList) {
            PostingSimpleResponseDto dto = new PostingSimpleResponseDto(
                    posting.getId(), posting.getTitle(), posting.getContents(), posting.getUserId());
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
                posting.getUserId());
    }



    @Transactional
    public PostingUpdateResponseDto updatePosting(Long posting_id, PostingUpdateRequestDto postingUpdateRequestDto) {
        Posting posting = postingRepository.findById(posting_id).orElseThrow(() -> new NullPointerException("없음"));
        posting.update(posting.getContents(), posting.getTitle());

        return new PostingUpdateResponseDto(posting.getId(), posting.getTitle(), posting.getContents(),
                posting.getUserId()
        );
    }

    @Transactional
    public void deletePosting(Long posting_id){
        postingRepository.deleteById(posting_id);

    }
}





