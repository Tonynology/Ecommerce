package project.Ecommerce.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignUp.Response> signUp(@RequestBody @Valid SignUp.Request request) {
    return ResponseEntity.ok(userService.signUp(request));
  }

  @PostMapping("/signin")
  public ResponseEntity<SignIn.Response> signIn(@RequestBody @Valid SignIn.Request request) {
    return ResponseEntity.ok(userService.signIn(request));
  }


}
