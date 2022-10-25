package hous.server.service.auth;

import hous.server.service.auth.dto.request.LoginDto;
import hous.server.service.auth.dto.request.SignUpDto;

public interface AuthService {

    Long signUp(SignUpDto request);

    Long login(LoginDto request);

    Long forceLogin(LoginDto request);
}
