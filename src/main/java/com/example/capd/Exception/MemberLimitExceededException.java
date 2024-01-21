package com.example.capd.Exception;

public class MemberLimitExceededException extends RuntimeException {
    public MemberLimitExceededException() {
        super("멤버 ID는 최대 6개까지만 허용됩니다.");
    }
}