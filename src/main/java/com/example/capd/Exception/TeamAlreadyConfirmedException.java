package com.example.capd.Exception;

public class TeamAlreadyConfirmedException extends RuntimeException {
    public TeamAlreadyConfirmedException(){
        super("팀이 확정되어 멤버 수정이 불가능 합니다.");
    }
}