package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;
import static project.Ecommerce.type.NotificationType.PRODUCT_UPDATE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.Ecommerce.dto.Notify;
import project.Ecommerce.entity.Notification;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.repository.EmitterRepository;
import project.Ecommerce.repository.NotificationRepository;
import project.Ecommerce.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

  private final EmitterRepository emitterRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Override
  public SseEmitter subscribe(String userEmail, String lastEventId) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    String emitterId = makeTimeIncludedId(user.getId());

    SseEmitter emitter =  emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    //오류 종류별 구독 취소 처리
    emitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); //네트워크 오류
    emitter.onTimeout(() -> emitterRepository.deleteById(emitterId)); //시간 초과
    emitter.onError((e) -> emitterRepository.deleteById(emitterId)); //오류

    // 503 에러를 방지하기 위한 더미 이벤트 전송
    String eventId = makeTimeIncludedId(user.getId());
    sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + user.getId() + "]");

    if (!lastEventId.isEmpty()) {
      Map<String, Object> events = emitterRepository.
          findAllEventCacheStartsWithUserId(String.valueOf(user.getId()));
      events.entrySet().stream()
          .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
          .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    return emitter;
  }

  @Override
  public void send(Notify.Request notificationRequest) {
    Notification notification = notificationRepository
        .save(createNotification(notificationRequest));

    String receiverId = String.valueOf(notification.getReceiver().getId());
    String eventId = makeTimeIncludedId(notification.getReceiver().getId());

    Map<String, SseEmitter> emitters = emitterRepository
        .findAllEmitterStartsWithUserId(receiverId);

    emitters.forEach((key, emitter) -> {
      emitterRepository.saveEventCache(key, notification);

      sendNotification(emitter, eventId, key, Notify.Response.builder()
          .message(createNotificationMessage(notificationRequest))
          .sendTime(LocalDateTime.now())
          .path(notificationRequest.getPath())
          .build());
    });
  }

  private String createNotificationMessage(Notify.Request notificationRequest) {

    if (notificationRequest.getNotificationType() == PRODUCT_UPDATE) {
      return "상품이 업데이트 되었습니다.";
    }
    return null;
  }

  private Notification createNotification(Notify.Request notificationRequest) {
    return Notification.builder()
        .receiver(notificationRequest.getReceiver())
        .notificationType(notificationRequest.getNotificationType())
        .path(notificationRequest.getPath())
        .build();
  }

  //단순 알림 전송
  private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {

    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name("sse")
          .data(data, MediaType.APPLICATION_JSON));
    } catch (IOException exception) {
      emitterRepository.deleteById(emitterId);
      emitter.completeWithError(exception);
    }
  }

  private String makeTimeIncludedId(Long userId) {
    return userId + "_" + System.currentTimeMillis();
  }


}
