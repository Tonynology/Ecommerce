package project.Ecommerce.security;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import project.Ecommerce.service.MemberDetailService;

@Slf4j
@ComponentScan
@RequiredArgsConstructor
public class GetAuthentication {

  private final MemberDetailService memberDetailService;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  /**
   * JWT 토큰에서 인증 정보 조회
   * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성해 Authentication 객체 반환
   */
  public Authentication getAuthentication(String token) {
    log.info("getAuthentication 시작");

    String userPrincipal = Jwts.parser().
        setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody().getSubject();
    UserDetails userDetails = memberDetailService.loadUserByUsername(userPrincipal);
    log.info("userDetails {}", userDetails.getUsername());

    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());
  }
}
