package com.example.capd.Exception;

public class TeamNotConfirmedException  extends RuntimeException {
    public TeamNotConfirmedException (){
        super("팀이 확정되지 않아 리뷰 작성이 불가능 합니다.");
    }
}