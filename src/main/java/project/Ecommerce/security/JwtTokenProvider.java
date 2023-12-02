package project.Ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

//    Claims claims = Jwts.claims().setSubject(authentication.getName());
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + accessExpirationTime);

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(now)
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  /**
   * Refresh 토큰 생성
   */
  public String createRefreshToken(String  email){
    log.info("createRefreshToken 시작");

//    Claims claims = Jwts.claims().setSubject(authentication.getName());
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + refreshExpirationTime);

    String refreshToken = Jwts.builder()
        .setSubject(email)
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
   * http 헤더로부터 bearer 토큰을 가져옴.
   */
  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // Jwt Token의 유효성 및 만료 기간 검사
  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (SignatureException e) {
      log.error("Invalid JWT signature", e);
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token", e);
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token", e);
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token", e);
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty.", e);
    }
    return false;
  }
}