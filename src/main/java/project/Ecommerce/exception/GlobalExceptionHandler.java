package project.Ecommerce.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserException.class)
  public UserException handleUserException(UserException e) {
    log.error("{} is occurred.", e.getErrorCode());

    return new UserException(e.getErrorCode(), e.getErrorMessage());
  }
}
