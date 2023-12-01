package project.Ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.Ecommerce.dto.ReIssue;
import project.Ecommerce.exception.TokenException;
import project.Ecommerce.redis.RefreshToken;
import project.Ecommerce.redis.RefreshTokenRepository;
import project.Ecommerce.security.JwtTokenProvider;
import project.Ecommerce.type.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public ReIssue.Response reIssue(ReIssue.Request request) {
    log.info("reIssue 시작");

    RefreshToken token =
        refreshTokenRepository.findByEmail(request.getRefreshToken())
            .orElseThrow(() ->
                new TokenException(ErrorCode.INVALID_TOKEN));

    String accessToken = jwtTokenProvider.createAccessToken(token.getEmail());

    return ReIssue.Response.builder()
        .accessToken(accessToken)
        .refreshToken(request.getRefreshToken())
        .build();
  }
}
