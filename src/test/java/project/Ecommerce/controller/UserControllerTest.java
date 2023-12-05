package project.Ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.Ecommerce.type.ErrorCode.INVALID_TOKEN;
import static project.Ecommerce.type.ErrorCode.USER_ALREADY_EXIST;
import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.Ecommerce.dto.ReIssue;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignUp;
import project.Ecommerce.exception.TokenException;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.security.JwtTokenProvider;
import project.Ecommerce.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @MockBean
  private UserService userService;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  @DisplayName("회원가입 성공")
  void successSignUp() throws Exception {
    //given
    SignUp.Request user = SignUp.Request.builder()
        .name("Jason")
        .password("abc1234")
        .email("jason@gmail.com")
        .location("서울 양천구")
        .build();

    given(userService.signUp(any()))
        .willReturn(SignUp.Response.builder()
            .name(user.getName())
            .message(user.getName() + "님이 회원가입 되었습니다.")
            .build());

    //when
    //then
    mockMvc.perform(post("/user/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andDo(print());
  }

  @Test
  @DisplayName("회원가입 실패 - 이미 가입한 회원")
  void failSignUp() throws Exception {
    //given
    SignUp.Request user = SignUp.Request.builder()
        .name("Jason")
        .password("abc1234")
        .email("jason@gmail.com")
        .location("서울 양천구")
        .build();

    given(userService.signUp(any()))
        .willThrow(new UserException(USER_ALREADY_EXIST));

    //when
    //then
    mockMvc.perform(post("/user/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andDo(print())
        .andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXIST"))
        .andExpect(jsonPath("$.errorMessage").value("이미 존재하는 회원입니다"))
        .andExpect(status().isOk());
  }


  @Test
  @DisplayName("로그인 성공")
  void successSignIn() throws Exception {
    //given
    String accessToken = "accessToken";
    String refreshToken = "refreshToken";

    SignIn.Request request = SignIn.Request.builder()
        .email("jason@gmail.com")
        .password("abc1234")
        .build();

    SignIn.Response response = SignIn.Response.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    given(userService.signIn(any()))
        .willReturn(response);

    //when
    //then
    mockMvc.perform(post("/user/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(accessToken))
        .andExpect(jsonPath("$.refreshToken").value(refreshToken))
        .andDo(print());
  }

  @Test
  @DisplayName("로그인 실패 - 존재하지 않는 이메알")
  void failSignIn() throws Exception {
    //given
    SignIn.Request request = SignIn.Request.builder()
        .email("jason@gmail.com")
        .password("abc1234")
        .build();

    given(userService.signIn(any()))
        .willThrow(new UserException(USER_NOT_FOUND));

    //when
    //then
    mockMvc.perform(post("/user/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"))
        .andExpect(jsonPath("$.errorMessage").value("회원가입이 안된 이메일입니다"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("토큰 재발급 성공")
  void successReIssue() throws Exception {
    //given
    String accessToken = "accessToken";
    String refreshToken = "refreshToken";

    ReIssue.Request request = ReIssue.Request.builder()
        .refreshToken(refreshToken)
        .build();

    ReIssue.Response response = ReIssue.Response.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    given(userService.reIssue(any()))
        .willReturn(response);

    //when
    //then
    mockMvc.perform(post("/user/reissue")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(accessToken))
        .andExpect(jsonPath("$.refreshToken").value(refreshToken))
        .andDo(print());
  }
}
