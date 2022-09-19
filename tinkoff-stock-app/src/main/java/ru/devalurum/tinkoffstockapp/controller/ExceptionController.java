package ru.devalurum.tinkoffstockapp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.secured.ValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.devalurum.tinkoffstockapp.domain.dto.ErrorDto;
import ru.devalurum.tinkoffstockapp.exception.StockNotFoundException;


@Hidden
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StockNotFoundException.class)
    public ErrorDto handleNotFound(Exception ex) {
        return new ErrorDto(ex.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorDto internalError(RuntimeException exception) {
        log.error("", exception);
        return new ErrorDto(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidateException.class)
    public ErrorDto badRequest(ValidateException exception) {
        return new ErrorDto(exception.getMessage());
    }
}
