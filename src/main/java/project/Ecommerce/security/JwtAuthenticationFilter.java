package project.Ecommerce.security;

import io.lettuce.core.RedisConnectionException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.Ecommerce.exception.TokenException;
import project.Ecommerce.type.ErrorCode;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private GetAuthentication getAuthentication;

  // Request로 들어오는 Jwt Token의 유효성을 검증하는 filter를 filterChain에 등록합니다.
  @Override
  public void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    String token = jwtTokenProvider.resolveToken(request);

    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {   // token 검증
        Authentication auth = getAuthentication.getAuthentication(token);    // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder에 인증 객체 저장
      }
    } catch (RedisConnectionException e) {
      SecurityContextHolder.clearContext();
      throw new TokenException(ErrorCode.REDIS_ERROR);
    } catch (Exception e) {
      throw new TokenException(ErrorCode.INVALID_JWT);
    }
    filterChain.doFilter(request, response);
  }
}