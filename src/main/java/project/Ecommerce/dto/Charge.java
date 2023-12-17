package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Charge {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {
    private double depositMoney;

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String userName;
    private double depositMoney;
    private double totalMoney;
    private String message;

    public static Response toResponse(
        double depositMoney, double totalMoney, String userName) {
      return Response.builder()
          .userName(userName)
          .depositMoney(depositMoney)
          .totalMoney(totalMoney)
          .message("지갑에 입급이 완료되었습니다.")
          .build();
    }
  }
}
