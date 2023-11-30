package project.Ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.Ecommerce.dto.ReIssue;
import project.Ecommerce.exception.TokenException;
import project.Ecommerce.security.GetAuthentication;
import project.Ecommerce.security.JwtTokenProvider;
import project.Ecommerce.type.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String, String> redisTemplate;
  private final GetAuthentication getAuthentication;

  @Override
  public ReIssue.Response reIssue(ReIssue.Request request) {
    log.info("reIssue 시작");

    // refresh Token 검증
    jwtTokenProvider.validateToken(request.getRefreshToken());
    // access Token에서 user 를 가져옴
    Authentication authentication = getAuthentication.getAuthentication(request.getRefreshToken());

    // Redis에서 저장된 refresh Token 값을 가져옴
    String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
    if (!redisRefreshToken.equals(request.getRefreshToken())) {
      throw new TokenException(ErrorCode.NOT_EXIST_REFRESH_JWT);
    }
    // 토큰 재발행
    return ReIssue.Response.builder()
        .accessToken(jwtTokenProvider.createAccessToken(authentication))
        .refreshToken(jwtTokenProvider.createRefreshToken(authentication))
        .build();
  }
}
