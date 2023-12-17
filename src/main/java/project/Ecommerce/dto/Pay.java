package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Pay {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String userName;
    private double remain;
    private String message;

    public static Response toResponse(String userName, double remain) {
      return Response.builder()
          .userName(userName)
          .remain(remain)
          .message("구매하였습니다.")
          .build();
    }
  }
}
