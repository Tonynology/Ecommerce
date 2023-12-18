package project.Ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.Ecommerce.dto.UserDetail;
import project.Ecommerce.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping("/subscribe/{userId}")
  public SseEmitter subscribe(
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return notificationService.subscribe(userEmail, lastEventId);
  }
}
