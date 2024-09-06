package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.comment.CommentCreateRequestDto;
import com.sparta.snsproject.dto.comment.CommentResponseDto;
import com.sparta.snsproject.dto.comment.CommentUpdateRequestDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.entity.Comment;
import com.sparta.snsproject.entity.Posting;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.exception.NotFoundException;
import com.sparta.snsproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommonService commonService;

    @Transactional
    public CommentResponseDto createComment(SignUser signUser, CommentCreateRequestDto requestDto) {
        User user = commonService.findUser(signUser.getId());
        Posting posting = commonService.findPosting(requestDto.getPostingId());
        Comment comment = new Comment(user, posting, requestDto);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto(saveComment);
    }

    public CommentResponseDto getComment(Long commentId) {
        Comment comment = findComment(commentId);
        return new CommentResponseDto(comment);
    }



    @Transactional
    public CommentResponseDto updateComment(SignUser signUser, Long commentId, CommentUpdateRequestDto requestDto) {
        Comment comment = findComment(commentId);
        commonService.confirmCreator(signUser.getId(), comment.getUser().getId(), false);
        comment.update(requestDto);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto(saveComment);
    }

    @Transactional
    public void deleteComment(SignUser signUser, Long commentId) {
        Comment comment = findComment(commentId);
        commonService.confirmCreator(signUser.getId(), comment.getUser().getId(), true);
        commentRepository.delete(comment);
    }

    public Page<CommentResponseDto> getUserComments(Long userid, int page, int size) {
        //page 속성 넣기
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("modifiedAt").descending());
        Page<Comment> commentList= commentRepository.findAllByUserId(userid, pageable);
        return commentList.map(CommentResponseDto::new);
    }

    public Page<CommentResponseDto> getPostingComments(Long postingId, int page, int size) {
        //page 속성 넣기
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Comment> commentList= commentRepository.findAllByPostingId(postingId, pageable);
        return commentList.map(CommentResponseDto::new);
    }

    private Comment findComment (Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("해당하는 아이디의 댓글이 존재하지 않습니다."));
    }
}
