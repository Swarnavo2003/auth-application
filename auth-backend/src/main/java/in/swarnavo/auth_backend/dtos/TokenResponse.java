package in.swarnavo.auth_backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    String refreshToken;
    String accessToken;
    long expiresIn;
    String tokenType = "Bearer";
    UserDto user;
}
