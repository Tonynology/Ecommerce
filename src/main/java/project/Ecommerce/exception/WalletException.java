package project.Ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.Ecommerce.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WalletException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String errorMessage;

    public WalletException(ErrorCode errorCode) {
      this.errorCode = errorCode;
      this.errorMessage = errorCode.getDescription();
    }
}
