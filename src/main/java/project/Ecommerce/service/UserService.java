package project.Ecommerce.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignUp;

public interface UserService extends UserDetailsService {

  SignUp.Response signUp(SignUp.Request request);
  SignIn.Response signIn(SignIn.Request request);

}
