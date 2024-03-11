package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.constant.Constants;
import ru.practicum.stats.dto.error.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class,
            BadRequestException.class,
            ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerBadRequestException(final RuntimeException exception) {
        log.error("Получен статус 400 Internal Server Error {}", exception.getMessage(), exception);
        return ApiError.builder()
                .errors(Arrays.toString(exception.getStackTrace()))
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason("Некорретный запрос")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerEntityUpdateException(final ValidationBadRequestException exception) {
        log.error("Получен статус 409 Internal Server Error {}", exception.getMessage(), exception);
        return ApiError.builder()
                .errors(Arrays.toString(exception.getStackTrace()))
                .status(HttpStatus.CONFLICT.toString())
                .reason("Конфликт с другим запросом или состоянием сервера")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerNotFoundException(final EntityNotFoundException exception) {
        log.error("error со статусом 404 {}", exception.getMessage());
        return ApiError.builder()
                .errors(Arrays.toString(exception.getStackTrace()))
                .status(HttpStatus.NOT_FOUND.toString())
                .reason("Объект на найден")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_PATTERN)))
                .build();
    }
}
