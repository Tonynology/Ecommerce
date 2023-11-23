package project.Ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.repository.UserRepository;
import project.Ecommerce.type.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public SignUp.Response signUp(SignUp.Request request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserException(ErrorCode.USER_ALREADY_EXIST);
    }
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    User user = userRepository.save(request.toEntity());

    log.info(request.getName() + " 회원가입");
    return SignUp.Response.toResponse(user.getName());
  }
}
