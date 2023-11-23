package project.Ecommerce.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_ALREADY_EXIST("이미 존재하는 회원입니다");

  private final String description;
}
