package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.INVALID_USER_PW;
import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;
import static project.Ecommerce.type.ErrorCode.USER_PASSWORD_INCORRECT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.TokenException;
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
  private final AuthenticationManager authenticationManager;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findUserByName(username)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
  }

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
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new UserException(USER_PASSWORD_INCORRECT);
    }
    log.info("유저 비밀번호 일치 이름 {}, 이메일 {}", user.getName(), user.getEmail());

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              user.getName(),
              user.getPassword()
          )
      );
      log.info("유저 이름 {}, 패스워드 {}", user.getName(), user.getPassword());

      return SignIn.Response.builder()
          .accessToken(jwtTokenProvider.createAccessToken(authentication))
          .refreshToken(jwtTokenProvider.createRefreshToken(authentication))
          .build();

    }catch(BadCredentialsException e){
      log.error("유저 비밀번호 불일치");
      throw new TokenException(INVALID_USER_PW);
    }
  }
}
