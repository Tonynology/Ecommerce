package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddWatchList {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String userEmail;
    private String productName;
    private String message;

    public static Response toResponse(String userEmail, String productName) {
      return Response.builder()
          .userEmail(userEmail)
          .productName(productName)
          .message("관심 목록에 추가되었습니다.")
          .build();
    }
  }
}
