package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateWallet {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String userName;
    private String message;

    public static Response toResponse(String userName) {
      return Response.builder()
          .userName(userName)
          .message("지갑이 만들어 졌습니다.")
          .build();
    }
  }
}
