package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Remain {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String userName;
    private double remainMoney;
    private String message;

    public static Response toResponse(String userName, double remainMoney) {
      return Response.builder()
          .userName(userName)
          .remainMoney(remainMoney)
          .message("잔액을 확인하였습니다.")
          .build();
    }
  }
}
