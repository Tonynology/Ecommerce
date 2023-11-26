package project.Ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignIn.Response;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.security.JwtTokenProvider;
import project.Ecommerce.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/signup")
  public ResponseEntity<SignUp.Response> signUp(@RequestBody @Valid SignUp.Request request) {
    return ResponseEntity.ok(userService.signUp(request));
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signIn(@RequestBody @Valid SignIn.Request request) {
    Response response = userService.signIn(request);
    return ResponseEntity.ok(jwtTokenProvider.createToken(response.getUserName()));
  }
}
