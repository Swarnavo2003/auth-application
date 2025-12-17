package in.swarnavo.auth_backend.services;

import in.swarnavo.auth_backend.dtos.LoginRequest;
import in.swarnavo.auth_backend.dtos.TokenResponse;
import in.swarnavo.auth_backend.dtos.UserDto;
import in.swarnavo.auth_backend.entities.User;
import in.swarnavo.auth_backend.repositories.UserRepository;
import in.swarnavo.auth_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public UserDto registerUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.createUser(userDto);
    }

    @Override
    public TokenResponse loginUser(LoginRequest loginRequest) {
        Authentication authenticate = authenticate(loginRequest);
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid Username or Password"));
        if(!user.isEnable()) {
            throw new DisabledException("User is disabled");
        }

        String accessToken = jwtService.generateAccessToken(user);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken("");
        tokenResponse.setExpiresIn(jwtService.getAccessTtlSeconds());
        tokenResponse.setUser(modelMapper.map(user, UserDto.class));

        return tokenResponse;
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Credentials");
        }
    }
}