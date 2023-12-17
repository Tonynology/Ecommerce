package project.Ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.Ecommerce.dto.ChatRoomDto;
import project.Ecommerce.dto.UserDetail;
import project.Ecommerce.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping("/{productId}")
  public ResponseEntity<ChatRoomDto.Response> createChatRoom(@PathVariable Long productId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(chatRoomService.createChatRoom(userEmail, productId));
  }


}

