package com.example.capd.Exception;

public class UserWithDesiredStackNotFoundException extends RuntimeException{
    public UserWithDesiredStackNotFoundException() {
        super("원하는 스택을 가지고 있는 유저가 존재하지 않습니다.");
    }
}
