package in.swarnavo.auth_backend.services;

import in.swarnavo.auth_backend.dtos.UserDto;

public interface AuthService {
    public UserDto registerUser(UserDto userDto);
}
