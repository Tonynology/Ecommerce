package project.Ecommerce.redis;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
  private final RedisTemplate redisTemplate;

  public void save(final RefreshToken refreshToken) {
    log.info("save");
    ValueOperations<String, String> valueOperations =
        redisTemplate.opsForValue();
    valueOperations.set(refreshToken.getRefreshToken()
        , refreshToken.getEmail());

    redisTemplate.expire(
        refreshToken.getRefreshToken(), 180L, TimeUnit.SECONDS);
  }

  public Optional<RefreshToken> findByEmail(
      final String refreshToken) {

    ValueOperations<String, String> valueOperations =
        redisTemplate.opsForValue();
    String mail = valueOperations.get(refreshToken);

    if (Objects.isNull(mail)) {
      return Optional.empty();
    }

    return Optional.of(new RefreshToken(refreshToken, mail));
  }
}
