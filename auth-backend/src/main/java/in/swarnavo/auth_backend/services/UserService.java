package in.swarnavo.auth_backend.services;

import in.swarnavo.auth_backend.dtos.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserDto userDto, String userId);
    void deleteUser(String userId);
    UserDto getUserById(String userId);
    List<UserDto> getAllUsers();
}
