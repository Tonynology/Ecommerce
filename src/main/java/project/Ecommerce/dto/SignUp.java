package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.Ecommerce.entity.User;


public class SignUp {
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {
    private String name;

    private String email;

    private String password;

    private String location;

    public User toEntity() {
      return User.builder()
          .name(this.getName())
          .email(this.getEmail())
          .password(this.getPassword())
          .location(this.getLocation())
          .build();
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {
    private String name;
    private String message;
    public static Response toResponse(String userName) {
      return Response.builder()
          .name(userName)
          .message(userName + "님이 회원가입 되었습니다.")
          .build();
    }
  }

}
