package com.sparta.snsproject.exception;

public class DuplicateNickNameException extends RuntimeException {
    public DuplicateNickNameException() {
        super("이미 사용중인 닉네임입니다.");
    }
}