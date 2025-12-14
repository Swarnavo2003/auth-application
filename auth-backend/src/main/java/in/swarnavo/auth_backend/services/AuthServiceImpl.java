package in.swarnavo.auth_backend.services;

import in.swarnavo.auth_backend.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // logic
        // verify email
        // verify password
        // default roles
        return userService.createUser(userDto);
    }
}