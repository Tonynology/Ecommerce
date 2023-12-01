package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.INVALID_USER_EMAIL;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.Ecommerce.entity.User;
import project.Ecommerce.exception.TokenException;
import project.Ecommerce.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> {
          log.error(INVALID_USER_EMAIL.getDescription());
          return new TokenException(INVALID_USER_EMAIL);
        });

    return user;
  }
}
