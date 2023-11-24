package project.Ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserServiceImpl userServiceImpl;

  @PostMapping("/signup")
  public ResponseEntity<SignUp.Response> signUp(@RequestBody @Valid SignUp.Request request) {
    return ResponseEntity.ok(userServiceImpl.signUp(request));
  }
}
