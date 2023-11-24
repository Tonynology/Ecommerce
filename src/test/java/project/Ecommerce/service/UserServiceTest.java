package project.Ecommerce.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.entity.User;
import project.Ecommerce.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private UserServiceImpl userServiceImpl;

  @Test
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
}
