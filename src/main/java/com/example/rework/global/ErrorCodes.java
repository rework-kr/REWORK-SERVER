package com.example.rework.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {
    DUPLICATE_ACCOUNT_EXCEPTION(HttpStatus.CONFLICT, "ACCOUNT_001", "DUPLICATE_ACCOUNT"),
    ENCRYPT_FAILED_EXCEPTION(HttpStatus.NOT_ACCEPTABLE, "ACCOUNT_002", "ENCRYPT_FAILED"),
    AUTHENTICATION_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_003", "AUTHENTICATION_FAILED"),
    OAUTH_AUTHENTICATION_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_004", "OAUTH_AUTHENTICATION_FAILED"),
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_005","TOKEN_AUTHENTICATION_FAILED"),
    NOT_FOUND_ACCOUNT_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_006","등록된 계정이 없음"),
    PASSWORD_NOT_MATCH(HttpStatus.NOT_FOUND,"ACCOUNT_007","비밀번호가 일치하지 않습니다"),
    REQUEST_PARAMETER_BIND_EXCEPTION(HttpStatus.BAD_REQUEST, "REQ_001", "PARAMETER_BIND_FAILED"),
    UnAuthorizedException(HttpStatus.UNAUTHORIZED,"REQ_002","권한이 없습니다"),
    DATABASE_VALIDATION_ERROR(HttpStatus.CONFLICT,"VALIDATION_001","DB ERROR"),
    INVALID_DISCORD_MESSAGE(HttpStatus.BAD_REQUEST,"DISCORD_001","DISCORD_MESSAGE_ERROR"),
    NOT_FOUND_MONTHLY_AGENDA_EXCEPTION(HttpStatus.NOT_FOUND,"AGENDA_001","해당하는 아젠다가 없습니다.");


    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCodes(final HttpStatus status, final String code, final String message){
        this.status = status;
        this.message = message;
        this.code = code;
    }
}