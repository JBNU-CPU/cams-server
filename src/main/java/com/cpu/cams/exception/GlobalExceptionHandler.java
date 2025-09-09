package com.cpu.cams.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 직접 정의한 CustomException을 처리하는 핸들러
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        log.error("CustomException occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(ex.getHttpStatus(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * @Valid 어노테이션을 사용한 유효성 검사 실패 시 발생하는 예외를 처리하는 핸들러
     * ResponseEntityExceptionHandler를 상속받아 오버라이드
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("MethodArgumentNotValidException occurred: {}", ex.getMessage());

        // 3. 어떤 필드에서 어떤 오류가 발생했는지 추출
        String errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", ")); // 4. 여러 오류를 쉼표로 연결

        // 5. 우리가 정의한 ErrorResponse 형식으로 변환
        ErrorResponse errorResponse = ErrorResponse.of(
                (HttpStatus) status,
                errorMessages,
                request.getDescription(false).replace("uri=", "")
        );

        // 6. 최종 ResponseEntity 객체로 감싸서 반환
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    /**
     * 모든 예외를 처리하는 핸들러 (가장 마지막에 위치해야 함)
     * 위에서 처리되지 않은 모든 예외를 여기서 처리합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
