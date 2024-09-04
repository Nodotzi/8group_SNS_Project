package com.sparta.snsproject.exception;

public class ExistFrandsName extends RuntimeException{
    public ExistFrandsName(){
        super("이미 친구요청이 들어온 유저 입니다. 요청을 수락해주세요");
    }

}
