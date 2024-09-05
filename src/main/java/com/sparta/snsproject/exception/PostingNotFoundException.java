package com.sparta.snsproject.exception;

public class PostingNotFoundException extends RuntimeException{
    public PostingNotFoundException() { super("게시물이 없습니다.");}
}
