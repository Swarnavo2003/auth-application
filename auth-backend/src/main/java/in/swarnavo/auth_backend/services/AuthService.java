package in.swarnavo.auth_backend.services;

import in.swarnavo.auth_backend.dtos.LoginRequest;
import in.swarnavo.auth_backend.dtos.TokenResponse;
import in.swarnavo.auth_backend.dtos.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public UserDto registerUser(UserDto userDto);
    public TokenResponse loginUser(LoginRequest loginRequest, HttpServletResponse response);
}
