package kartly_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    public String accessToken;
    public String refreshToken;
}
