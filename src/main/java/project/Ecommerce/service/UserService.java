package project.Ecommerce.service;

import project.Ecommerce.dto.ReIssue;
import project.Ecommerce.dto.SignIn;
import project.Ecommerce.dto.SignUp;

public interface UserService {

  SignUp.Response signUp(SignUp.Request request);
  SignIn.Response signIn(SignIn.Request request);
  ReIssue.Response reIssue(ReIssue.Request request);


}
