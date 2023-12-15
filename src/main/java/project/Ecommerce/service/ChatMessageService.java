package project.Ecommerce.service;

import project.Ecommerce.dto.ChatMessageDto;

public interface ChatMessageService {

  ChatMessageDto.Response sendChatMessage(ChatMessageDto.Request request, String userEmail, Long chatRoomId);
}
