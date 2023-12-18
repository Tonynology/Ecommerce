package project.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Ecommerce.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
