package project.Ecommerce.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.Ecommerce.dto.ReIssue;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.TokenException;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.redis.RefreshToken;
import project.Ecommerce.redis.RefreshTokenRepository;
import project.Ecommerce.repository.UserRepository;
import project.Ecommerce.security.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RefreshTokenRepository refreshTokenRepository;

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @InjectMocks
  private UserServiceImpl userServiceImpl;

  @Test
  @DisplayName("회원가입 성공")
  void successSignUp() {
    //given
    SignUp.Request request = SignUp.Request.builder()
        .name("Jason")
        .email("jason@gmail.com")
        .password("abc1234")
        .location("서울")
        .build();

    given(userRepository.save(any()))
        .willReturn(User.builder()
            .name("Jason")
            .email("jason@gmail.com")
            .password("abc1234")
            .location("서울")
            .build());

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    //when
    SignUp.Response response = userServiceImpl.signUp(request);

    //then
    verify(userRepository, times(1)).save(captor.capture());
    assertEquals("Jason", response.getName());
    assertEquals("Jason", captor.getValue().getName());
  }

  @Test
  @DisplayName("로그인 성공")
  void successSignIn() {
    //given
    String email = "Jason@gmail.com";
    String password = "abc1234";
    String accessToken = "accessToken";
    String refreshToken="refreshToken";

    SignIn.Request request = SignIn.Request.builder()
        .email(email)
        .password(password)
        .build();

    User mockUser = new User();
    mockUser.setEmail(email);
    mockUser.setPassword(password);

    when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(mockUser));
    when(bCryptPasswordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);
    when(jwtTokenProvider.createAccessToken(email)).thenReturn(accessToken);
    when(jwtTokenProvider.createRefreshToken(email)).thenReturn(refreshToken);

    //when
    SignIn.Response response = userServiceImpl.signIn(request);

    //then
    assertEquals(accessToken, response.getAccessToken());
    assertEquals(refreshToken, response.getRefreshToken());
  }

  @Test
  @DisplayName("로그인 실패 - 사용자를 찾을 수 없음")
  void failSignInUserNotFound() {
    // given
    String email = "nonexisting@example.com";
    String password = "abc1234";

    SignIn.Request request = SignIn.Request.builder()
        .email(email)
        .password(password)
        .build();

    when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

    //when
    //then
    assertThrows(UserException.class, () -> userServiceImpl.signIn(request));
  }

  @Test
  @DisplayName("로그인 실패 - 잘못된 비밀번호")
  void failSignInIncorrectPassword() {
    //given
    String email = "Jason@gmail.com";
    String password = "wrongPassword";

    User mockUser = new User();
    mockUser.setEmail(email);
    mockUser.setPassword(password);

    SignIn.Request request = SignIn.Request.builder()
        .email(email)
        .password(password)
        .build();

    when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(mockUser));
    when(bCryptPasswordEncoder.matches(password, mockUser.getPassword())).thenReturn(false);

    // when
    // then
    assertThrows(UserException.class, () -> userServiceImpl.signIn(request));
  }

  @Test
  @DisplayName("토큰 재발급 성공")
  void successReIssue() {
    // Given
    String email = "user@example.com";
    String refreshToken = "refreshToken";
    String newAccessToken = "newAccessToken";
    RefreshToken mockRefreshToken = new RefreshToken(refreshToken, email);

    ReIssue.Request request = ReIssue.Request.builder()
        .refreshToken(refreshToken)
        .build();

    when(refreshTokenRepository.findByEmail(refreshToken)).thenReturn(Optional.of(mockRefreshToken));
    when(jwtTokenProvider.createAccessToken(email)).thenReturn(newAccessToken);

    // When
    ReIssue.Response response = userServiceImpl.reIssue(request);

    // Then
    assertEquals(newAccessToken, response.getAccessToken());
    assertEquals(refreshToken, response.getRefreshToken());
  }

  @Test
  @DisplayName("토큰 재발급 실패 - 유효하지 않은 토큰")
  void failReIssueInvalidToken() {
    // Given
    String refreshToken = "invalidToken";
    ReIssue.Request request = ReIssue.Request.builder()
        .refreshToken(refreshToken)
        .build();

    when(refreshTokenRepository.findByEmail(refreshToken)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(TokenException.class, () -> userServiceImpl.reIssue(request));
  }
}
