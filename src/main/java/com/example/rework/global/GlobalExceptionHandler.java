package com.example.rework.global;

import com.example.rework.global.dto.CommonResponse;
import com.example.rework.global.error.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.net.BindException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 회원가입 시, 회원 계정 정보가 중복 되었을때
     */
    @ExceptionHandler(DuplicateAccountException.class)
    protected ResponseEntity<?> handleDuplicateAccountException(DuplicateAccountException ex) {
        log.error("handleDuplicateAccountException :: ");

        ErrorCodes errorCode = ErrorCodes.DUPLICATE_ACCOUNT_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     * 중복된 페이징번호
     */
    @ExceptionHandler(AlreadyPagingIdException.class)
    protected ResponseEntity<?> alreadyPagingIdExceptionException(AlreadyPagingIdException ex) {
        log.error("ALREADY_PAGINGID_ERROR :: ");

        ErrorCodes errorCode = ErrorCodes.ALREADY_PAGINGID_ERROR;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 패스워드 불일치 에러
     */
    @ExceptionHandler(PasswordNotMatchException.class)
    protected ResponseEntity<?> passwordUnchangedException(PasswordNotMatchException ex) {
        log.error("PasswordUnchangedException :: ");

        ErrorCodes errorCode = ErrorCodes.PASSWORD_NOT_MATCH;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    /**
     * discord 전송 오류
     */
    @ExceptionHandler(InvalidDiscordMessage.class)
    protected ResponseEntity<?> invalidDiscordMessage(InvalidDiscordMessage ex) {
        log.error("InvalidDiscordMessage :: ");

        ErrorCodes errorCode = ErrorCodes.INVALID_DISCORD_MESSAGE;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     * Handle SQL exceptions
     */
    @ExceptionHandler({DataAccessException.class, SQLException.class})
    protected ResponseEntity<?> handleSqlException(Exception ex) {
        log.error("SQL Exception occurred: {}", ex.getMessage());

        ErrorCodes errorCode = ErrorCodes.DATABASE_VALIDATION_ERROR;
        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(ex.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<?> handleUnAuthorizedException(UnAuthorizedException ex) {
        log.error("handleUnAuthorizedException :: ");
        ErrorCodes errorCode = ErrorCodes.UnAuthorizedException;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     * 리퀘스트 파라미터 바인딩이 실패했을때
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<?> handleRequestParameterBindException(BindException ex) {
        log.error("handleRequestParameterBindException :: ");

        ErrorCodes errorCode = ErrorCodes.REQUEST_PARAMETER_BIND_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 사용자 인증이 실패했을때
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handleAuthenticationException() {
        log.error("AuthenticationException :: ");
        ErrorCodes errorCode = ErrorCodes.AUTHENTICATION_FAILED_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 비밀번호가 일치않았을 때
     */
    @ExceptionHandler(PasswordNotMatchException.class)
    protected ResponseEntity<?> PasswordNotMatchException() {
        log.error("PasswordNotMatchException :: ");
        ErrorCodes errorCode = ErrorCodes.PASSWORD_NOT_MATCH;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     * 계정을 찿을 수 없을 때
     */
    @ExceptionHandler(NotFoundAccountException.class)
    protected ResponseEntity<?> handleNotFoundAccountException(NotFoundAccountException ex) {

        log.error("handleNotFoundAccountException");
        ErrorCodes errorCode = ErrorCodes.NOT_FOUND_ACCOUNT_EXCEPTION;
        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     * 유효성검사에 실패하는
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> argumentNotValidException(BindingResult bindingResult, MethodArgumentNotValidException ex) {
        log.error("argumentNotValidException :: ");
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ErrorCodes errorCode = ErrorCodes.REQUEST_PARAMETER_BIND_EXCEPTION;

        List<String> errorMessages = fieldErrors.stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorMessages.toString())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     * 주로 파일 업로드나 멀티파트 요청에서 파트나 매개변수가 누락된 경우에 해당 예외
     */

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> missingServletRequestPartException(MissingServletRequestPartException exception) {
        log.error("MissingServletRequestPartException = {}", exception);
        return ResponseEntity.badRequest().body("MissingServletRequestPartException");
    }

    /**
     * 유효성검사 타입 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException = {}", exception);
        return ResponseEntity.badRequest().body("잘못된 형식의 값입니다.");
    }

    /**
     * 아젠다를 찿을 수 없을 때
     */
    @ExceptionHandler(NotFoundAgendaException.class)
    protected ResponseEntity<?> handleNotFoundAgendaException(NotFoundAgendaException ex) {

        log.error("handleNotFoundAgendaException");
        ErrorCodes errorCode = ErrorCodes.NOT_FOUND_MONTHLY_AGENDA_EXCEPTION;
        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}