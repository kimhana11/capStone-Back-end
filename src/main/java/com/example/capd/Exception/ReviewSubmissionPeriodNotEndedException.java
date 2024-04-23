package com.example.capd.Exception;

public class ReviewSubmissionPeriodNotEndedException extends RuntimeException {
    public ReviewSubmissionPeriodNotEndedException(){
        super("심사기간이 끝나지 않아 리뷰를 작성할 수 없습니다");
    }
    public ReviewSubmissionPeriodNotEndedException(int n){
        super("접수기간이 끝나지 않아 리뷰를 작성할 수 없습니다");
    }
}