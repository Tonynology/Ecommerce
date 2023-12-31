package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.CANNOT_SEND_CHAT_YOURSELF;
import static project.Ecommerce.type.ErrorCode.CHATROOM_ALREADY_EXIST;
import static project.Ecommerce.type.ErrorCode.PRODUCT_NOT_FOUND;
import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.Ecommerce.dto.ChatRoomDto;
import project.Ecommerce.entity.ChatRoom;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.ChatRoomException;
import project.Ecommerce.exception.ProductException;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.repository.ChatRoomRepository;
import project.Ecommerce.repository.ProductRepository;
import project.Ecommerce.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ProductRepository productRepository;

  @Override
  public ChatRoomDto.Response createChatRoom(String userEmail, Long productId) {

    User sender = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

    if (chatRoomRepository.existsChatRoomBySenderAndProduct(sender, product)) {
      throw new ChatRoomException(CHATROOM_ALREADY_EXIST);
    }

    User receiver = product.getSeller();
    if (sender.equals(receiver)) {
      throw new ChatRoomException(CANNOT_SEND_CHAT_YOURSELF);
    }

    chatRoomRepository.save(ChatRoom.builder()
            .sender(sender)
            .receiver(receiver)
            .product(product)
            .build());

    return ChatRoomDto.Response.toResponse(
        sender.getName(), receiver.getName(), product.getTitle());
  }
}
