package project.Ecommerce.service;

import project.Ecommerce.dto.ChatRoomDto;

public interface ChatRoomService {

  ChatRoomDto.Response createChatRoom(String userEmail, Long productId);
}
