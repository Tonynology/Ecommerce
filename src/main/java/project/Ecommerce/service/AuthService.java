package project.Ecommerce.service;

import project.Ecommerce.dto.ReIssue;

public interface AuthService {

  ReIssue.Response reIssue(ReIssue.Request request);
}
