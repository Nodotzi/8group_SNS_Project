package com.sparta.snsproject.exception;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException() {super("잘못된 비밀번호입니다.");}
}
