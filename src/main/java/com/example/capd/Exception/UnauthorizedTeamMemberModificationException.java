package com.example.capd.Exception;

public class UnauthorizedTeamMemberModificationException extends RuntimeException {
    public UnauthorizedTeamMemberModificationException() {
        super("해당 유저는 팀 멤버 수정 권한이 없습니다.");
    }
}