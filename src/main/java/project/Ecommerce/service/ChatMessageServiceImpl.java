package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.CHATROOM_NOT_EXIST;
import static project.Ecommerce.type.ErrorCode.UNAUTHORIZED_USER_IN_CHATROOM;
import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.Ecommerce.dto.ChatMessageDto;
import project.Ecommerce.dto.ChatMessageDto.Response;
import project.Ecommerce.dto.ChatRoomDto;
import project.Ecommerce.entity.ChatRoom;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.ChatRoomException;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.repository.ChatMessageRepository;
import project.Ecommerce.repository.ChatRoomRepository;
import project.Ecommerce.repository.ProductRepository;
import project.Ecommerce.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public ChatMessageDto.Response sendChatMessage(
      ChatMessageDto.Request request, String userEmail, Long chatRoomId) {

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new ChatRoomException(CHATROOM_NOT_EXIST));

    User sender = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (!(chatRoom.getSender().equals(sender) || chatRoom.getReceiver().equals(sender))) {
      throw new ChatRoomException(UNAUTHORIZED_USER_IN_CHATROOM);
    }

    chatMessageRepository.save(request.toEntity(chatRoom, sender));

    Response response = Response.toResponse(
        sender.getName(), chatRoom.getReceiver().getName(),
        chatRoom.getProduct().getTitle(), request.getContent());

    messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, response);

    return response;
  }
}
