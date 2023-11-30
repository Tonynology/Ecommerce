package project.Ecommerce.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.Ecommerce.dto.ReIssue;
import project.Ecommerce.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/reissue")
  public ResponseEntity<ReIssue.Response> reIssue(@RequestBody @Valid ReIssue.Request request) {
    log.info("reissue controller 시작");
    return ResponseEntity.ok(authService.reIssue(request));
  }
}
