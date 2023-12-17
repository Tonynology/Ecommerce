package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Delete {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String userName;
    private String productName;
    private String message;
    public static Response toResponse(String userName, String productName) {
      return Response.builder()
          .userName(userName)
          .productName(productName)
          .message("상품이 삭제되었습니다.")
          .build();
    }
  }
}
