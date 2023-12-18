package project.Ecommerce.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.Ecommerce.dto.Notify;

public interface NotificationService {

  SseEmitter subscribe(String userEmail, String lastEventId);

  void send(Notify.Request notificationRequest);
}
