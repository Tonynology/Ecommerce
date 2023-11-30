package project.Ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.Ecommerce.type.ErrorCode.USER_ALREADY_EXIST;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.Ecommerce.dto.SignUp;
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
}
