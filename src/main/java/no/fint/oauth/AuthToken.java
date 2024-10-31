package no.fint.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthToken {

    private final String accessToken;
    private final String tokenType;
    private final int expiresIn;
    private final String acr;
    private final String scope;

}
