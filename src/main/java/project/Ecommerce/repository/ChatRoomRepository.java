package project.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Ecommerce.entity.ChatRoom;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  boolean existsChatRoomBySenderAndProduct(User sender, Product product);
}
