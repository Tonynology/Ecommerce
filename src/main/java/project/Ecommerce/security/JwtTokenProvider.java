package project.Ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import project.Ecommerce.service.UserService;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

  private final long TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 10; // 10시간

  @Value("spring.jwt.secret")
  private String secretKey;

  private final UserService userService;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    System.out.println("_----------------------------------------------------");
    log.info("Secret Key: {}", secretKey);
  }

  // Jwt 토큰 생성
  public String createToken(String userPk) {
    Claims claims = Jwts.claims().setSubject(userPk);

    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims) // 정보 저장
        .setIssuedAt(now)   // 토큰 발행 시간 정보
        .setExpiration(new Date(now.getTime() + TOKEN_VALID_MILISECOND)) // 만료 시간
        .signWith(SignatureAlgorithm.HS512, secretKey) // 암호화 알고리즘, secret 값
        .compact(); // Token 생성
  }

  // JWT 토큰에서 인증 정보 조회
  // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // Jwt 토큰에서 user PK로 사용된 값을 추출한다
  public String getUserPk(String token) {
    return Jwts.parser().setSigningKey(secretKey)
        .parseClaimsJws(token).getBody().getSubject();
  }

  // 헤더정보에서 Authorization의 값을 추출한다.
  public String resolveToken(HttpServletRequest req) {
    return req.getHeader("Authorization");
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