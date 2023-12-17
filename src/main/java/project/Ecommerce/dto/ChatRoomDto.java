package project.Ecommerce.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ChatRoomDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String senderName;
    private String receiverName;
    private String productName;
    private String message;

    public static Response toResponse(String senderName, String receiverName, String productName) {
      return Response.builder()
          .senderName(senderName)
          .receiverName(receiverName)
          .productName(productName)
          .message("채팅방이 만들어졌습니다.")
          .build();
    }
  }
}
