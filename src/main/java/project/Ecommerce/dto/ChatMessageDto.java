package project.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.Ecommerce.entity.ChatMessage;
import project.Ecommerce.entity.ChatRoom;
import project.Ecommerce.entity.User;

public class ChatMessageDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {
    private String content;

    public ChatMessage toEntity(ChatRoom chatRoom, User sender) {
      return ChatMessage.builder()
          .chatRoom(chatRoom)
          .sender(sender)
          .content(this.content)
          .build();
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String senderName;
    private String receiverName;
    private String productName;
    private String content;

    public static Response toResponse(
        String senderName, String receiverName, String productName, String content) {
      return Response.builder()
          .senderName(senderName)
          .receiverName(receiverName)
          .productName(productName)
          .content(content)
          .build();
    }
  }
}
