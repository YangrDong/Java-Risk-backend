package com.example.vueadminjava.common.exception;

import com.example.vueadminjava.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
//这个注解标记了该类为全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 指定响应状态码, 如当捕获到 AccessDeniedException 异常时，返回的 HTTP 状态码为 403 Forbidden。
    @ResponseStatus(HttpStatus.FORBIDDEN)
    // 这个注解指定了要捕获的异常类型为 AccessDeniedException，当出现这个类型的异常时，会执行下方的 handler 方法。
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result handler(AccessDeniedException e) {
        log.info("security权限不足：----------------{}", e.getMessage());
        return Result.fail("权限不足");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.info("实体校验异常：----------------{}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常：----------------{}", e.getMessage());
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常：----------------{}", e.getMessage());
        return Result.fail(e.getMessage());
    }
}
