package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.comment.CommentRequestDto;
import com.sparta.snsproject.dto.comment.CommentResponseDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.entity.Comment;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.exception.NotFoundException;
import com.sparta.snsproject.repository.CommentRepository;
import com.sparta.snsproject.repository.PostingRepository;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostingRepository postingRepository;

    @Transactional
    public CommentResponseDto createComment(SignUser signUser, CommentRequestDto requestDto) {
        User user = userRepository.findById(signUser.getId()).orElseThrow(()-> new NotFoundException("해당하는 아이디의 유저가 존재하지 않습니다."));
        Posting posting = postingRepository.findById(requestDto.getPostingId()).orElseThrow(()->new NotFoundException("해당하는 아이디의 게시물이 존재하지 않습니다."));
        Comment comment = new Comment(user, posting, requestDto);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto(saveComment);
    }

    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("해당하는 아이디의 댓글이 존재하지 않습니다."));
        return new CommentResponseDto(comment);
    }



    @Transactional
    public CommentResponseDto updateComment(SignUser signUser, Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("해당하는 아이디의 댓글이 존재하지 않습니다."));
        if(!Objects.equals(signUser.getId(), comment.getUser().getId())) throw new IllegalArgumentException("작성자가 아니므로 수정이 불가능합니다.");
        comment.update(requestDto);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto(saveComment);
    }

    @Transactional
    public void deleteComment(SignUser signUser, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("해당하는 아이디의 댓글이 존재하지 않습니다."));
        if(!Objects.equals(signUser.getId(), comment.getUser().getId())) throw new IllegalArgumentException("작성자가 아니므로 삭제가 불가능합니다.");
        commentRepository.delete(comment);
    }

    public Page<CommentResponseDto> getUserComments(Long userid, int page, int size) {
        //page 속성 넣기
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Comment> commentList= commentRepository.findAllByUserId(userid, pageable);
        return commentList.map(CommentResponseDto::new);
    }

    public Page<CommentResponseDto> getPostingComments(Long postingId, int page, int size) {
        //page 속성 넣기
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Comment> commentList= commentRepository.findAllByPostingId(postingId, pageable);
        return commentList.map(CommentResponseDto::new);
    }
}
