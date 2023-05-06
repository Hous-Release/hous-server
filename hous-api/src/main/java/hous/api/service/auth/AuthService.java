package hous.api.service.auth;

import hous.api.service.auth.dto.request.LoginDto;
import hous.api.service.auth.dto.request.SignUpDto;

public interface AuthService {

	Long signUp(SignUpDto request);

	Long login(LoginDto request);

	Long forceLogin(LoginDto request);
}
