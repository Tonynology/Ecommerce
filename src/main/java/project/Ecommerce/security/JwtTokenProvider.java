package project.Ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.Ecommerce.redis.RefreshToken;
import project.Ecommerce.redis.RefreshTokenRepository;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

  private final RefreshTokenRepository refreshTokenRepository;


  @Value("${spring.jwt.secret}")
  private String secretKey;

  @Value("${spring.jwt.token.access-expiration-time}")
  private long accessExpirationTime;

  @Value("${spring.jwt.token.refresh-expiration-time}")
  private long refreshExpirationTime;


  /**
   * Access 토큰 생성
   */
  public String createAccessToken(String email){
    log.info("createAccessToken 시작");

    Claims claims = Jwts.claims().setSubject(email);
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + accessExpirationTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  /**
   * Refresh 토큰 생성
   */
  public String createRefreshToken(String email){
    log.info("createRefreshToken 시작");

    Claims claims = Jwts.claims().setSubject(email);
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + refreshExpirationTime);

    String refreshToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();

    refreshTokenRepository.save(
        new RefreshToken(refreshToken, email)
    );
    return refreshToken;
  }

  /**
   * bearer 토큰으로부터 토큰 분리
   */
  public String resolveToken(String token) {

    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
      token =  token.substring(7);
    }
    log.info("Token : {}", token);
    return token;
  }

  public String getUserEmail(String token) {
    String resolvedToken = resolveToken(token);
    return parseClaims(resolvedToken).getSubject();
  }

  // 토큰 파싱
  public Claims parseClaims(String token) {
    log.info("parseClaims 시작");
    try {
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // Jwt Token의 유효성 및 만료 기간 검사
  public boolean validateToken(String jwtToken) {
    log.info("validateToken 시작");
    try {
      return !parseClaims(jwtToken).getExpiration().before(new Date());

    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token", e);
      return false; // 만료된 토큰은 false를 반환
    } catch (SignatureException | MalformedJwtException |
             UnsupportedJwtException | IllegalArgumentException e) {
      log.error("Invalid JWT: " + e.getMessage());
    }
    return false;
  }
}