package hous.server.service.auth;

import hous.server.service.auth.dto.request.LoginDto;

public interface AuthService {

    Long login(LoginDto request);
}
