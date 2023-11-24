package project.Ecommerce.service;

import project.Ecommerce.dto.SignUp.Request;
import project.Ecommerce.dto.SignUp.Response;

public interface UserService {

  Response signUp(Request request);
}
