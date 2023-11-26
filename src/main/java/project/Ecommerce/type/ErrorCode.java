package project.Ecommerce.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_ALREADY_EXIST("이미 존재하는 회원입니다"),
  USER_NOT_FOUND("회원가입이 안된 이메일입니다"),
  USER_PASSWORD_INCORRECT("비밀번호가 일치하지 않습니다");

  private final String description;
}
