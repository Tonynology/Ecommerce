package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReIssue {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {
    private String accessToken;
    private String refreshToken;
  }
}
