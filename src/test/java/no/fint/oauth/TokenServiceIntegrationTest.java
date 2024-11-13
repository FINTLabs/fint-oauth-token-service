package no.fint.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = "fint.oauth.enabled=false")
public class TokenServiceIntegrationTest {

    @Autowired(required = false)
    private TokenService tokenService;

    @Test
    void shouldDisableTokenServiceWhenFintOauthEnabledIsFalse() {
        // When
        boolean isDisabled = (tokenService == null);

        // Then
        assertNull(tokenService, "TokenService should be disabled when fint.oauth.enabled is set to false");
    }
}
