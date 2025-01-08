package no.fint.oauth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class TokenInstance {

    private final TokenClient tokenClient;
    private final OAuthTokenProps props;
    private AuthToken authToken;

    @PostConstruct
    public void init() {
        if (ObjectUtils.isEmpty(props.getAccessTokenUri())) {
            log.info("No Access-token-url configured, will not initialize access token");
        } else {
            refreshToken();
        }
    }

    public boolean isNull() {
        return authToken == null;
    }

    public boolean hasExpired() {
        Duration duration = Duration.between(Instant.now(), Instant.ofEpochMilli(authToken.expirationTimestampMillis()));
        return duration.isNegative() || duration.getSeconds() < 30;
    }

    public String getAccessToken() {
        return authToken.accessToken();
    }

    public void refreshToken() {
        ResponseEntity<AuthToken> response = tokenClient.getAuthToken();
        if (response.getStatusCode().is2xxSuccessful()) {
            authToken = response.getBody();
        } else {
            throw new IllegalStateException("Unable to refresh token");
        }
    }

}
