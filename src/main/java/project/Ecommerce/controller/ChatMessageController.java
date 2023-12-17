package project.Ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.Ecommerce.dto.ChatMessageDto;
import project.Ecommerce.dto.ChatMessageDto.Response;
import project.Ecommerce.dto.UserDetail;
import project.Ecommerce.service.ChatMessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatMessageController {

  private final ChatMessageService chatMessageService;

  @MessageMapping("/send/{chatRoomId}")
  public ResponseEntity<Response> sendChatMessage(
      @DestinationVariable Long chatRoomId,
      ChatMessageDto.Request request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(chatMessageService.sendChatMessage(request, userEmail, chatRoomId));
  }
}
