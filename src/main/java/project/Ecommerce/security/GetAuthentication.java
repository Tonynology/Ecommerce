package project.Ecommerce.security;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import project.Ecommerce.service.UserService;

@Component
@RequiredArgsConstructor
public class GetAuthentication {

  private final UserService userService;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  /**
   * JWT 토큰에서 인증 정보 조회
   * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성해 Authentication 객체 반환
   */
  public Authentication getAuthentication(String token) {
    String userPrincipal = Jwts.parser().
        setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody().getSubject();
    UserDetails userDetails = userService.loadUserByUsername(userPrincipal);

    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());
  }
}
