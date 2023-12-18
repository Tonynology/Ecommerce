package project.Ecommerce.dto;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.Ecommerce.entity.User;
import project.Ecommerce.type.NotificationType;

public class Notify {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {
    private User receiver;
    private NotificationType notificationType;
    private Map<String, String> params;
    private String path;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {

    private String message;
    private LocalDateTime sendTime;
    private String path;
  }
}
