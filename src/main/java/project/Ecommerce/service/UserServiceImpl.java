package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;
import static project.Ecommerce.type.ErrorCode.USER_PASSWORD_INCORRECT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.repository.UserRepository;
import project.Ecommerce.security.JwtTokenProvider;
import project.Ecommerce.type.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;


  @Override
  public SignUp.Response signUp(SignUp.Request request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserException(ErrorCode.USER_ALREADY_EXIST);
    }
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    User user = userRepository.save(request.toEntity());

    log.info("{} 회원가입", request.getName());
    return SignUp.Response.toResponse(user.getName());
  }

  @Override
  public SignIn.Response signIn(SignIn.Request request) {
    User user = userRepository.findUserByEmail(request.getEmail())
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new UserException(USER_PASSWORD_INCORRECT);
    }

    return SignIn.Response.builder()
        .accessToken(jwtTokenProvider.createAccessToken(user.getEmail()))
        .refreshToken(jwtTokenProvider.createRefreshToken(user.getEmail()))
        .build();

    }

}
