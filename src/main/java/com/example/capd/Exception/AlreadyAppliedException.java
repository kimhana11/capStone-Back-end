package com.example.capd.Exception;

public class AlreadyAppliedException extends RuntimeException {
    public AlreadyAppliedException() {
        super("이미 참여신청을 한 상태입니다.");
    }
}