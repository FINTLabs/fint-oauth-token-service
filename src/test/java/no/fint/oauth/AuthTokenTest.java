package no.fint.oauth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenTest {

    @Test
    public void testAuthTokenCreation() {
        String accessToken = "sampleAccessToken";
        String tokenType = "Bearer";
        int expiresIn = 3600;
        String acr = "Level4";
        String scope = "read write";

        AuthToken authToken = new AuthToken(accessToken, tokenType, expiresIn, acr, scope);

        assertNotNull(authToken);
        assertEquals(accessToken, authToken.accessToken());
        assertEquals(tokenType, authToken.tokenType());
        assertEquals(expiresIn, authToken.expiresIn());
        assertEquals(acr, authToken.acr());
        assertEquals(scope, authToken.scope());
    }
}
