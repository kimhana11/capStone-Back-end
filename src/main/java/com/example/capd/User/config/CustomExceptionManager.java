package com.example.capd.User.config;

import com.example.capd.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionManager {
    @ExceptionHandler(MemberLimitExceededException.class)
    public ResponseEntity<CommonResponse> MemberException(MemberLimitExceededException e) {
        CommonResponse res = CommonResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .httpStatus(HttpStatus.CONFLICT)
                .message(e.getMessage()).build();
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @ExceptionHandler(AlreadyAppliedException.class)
    public ResponseEntity<CommonResponse> AlreadyException(AlreadyAppliedException e) {
        CommonResponse res = CommonResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage()).build();
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedTeamMemberModificationException.class)
    public ResponseEntity<CommonResponse> TeamMemberModificationException(UnauthorizedTeamMemberModificationException e) {
        CommonResponse res = CommonResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(e.getMessage()).build();
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
    @ExceptionHandler(ReviewSubmissionPeriodNotEndedException.class)
    public ResponseEntity<CommonResponse> TeamNotException(ReviewSubmissionPeriodNotEndedException e) {
        CommonResponse res = CommonResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(e.getMessage()).build();
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
    @ExceptionHandler(TeamAlreadyConfirmedException.class)
    public ResponseEntity<CommonResponse> TeamAlreadyException(TeamAlreadyConfirmedException e) {
        CommonResponse res = CommonResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(e.getMessage()).build();
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
