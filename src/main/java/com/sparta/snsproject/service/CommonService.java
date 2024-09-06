package com.sparta.snsproject.service;

import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.exception.NotFoundException;
import com.sparta.snsproject.repository.PostingRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommonService {
    private final UserRepository userRepository;
    private final PostingRepository postingRepository;

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    public Posting findPosting(Long postingId) {
        return postingRepository.findById(postingId)
                .orElseThrow(() -> new NotFoundException("해당하는 아이디의 게시물이 없습니다."));
    }

    public void confirmCreator(Long userAId, Long userBId, boolean delete) {
        if (!Objects.equals(userAId, userBId)){
            if(delete) throw new IllegalArgumentException("작성자가 아니므로 삭제가 불가능합니다.");
            else throw new IllegalArgumentException("작성자가 아니므로 수정이 불가능합니다.");
        }
    }
}
