package no.fint.oauth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "fint.oauth.enabled", havingValue = "true")
public class TokenInstance {

    private final MultiValueMap<String, String> formData = createFormData();
    private final RestClient oauthRestClient;
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
        ResponseEntity<AuthToken> response = oauthRestClient.post()
                .uri(props.getAccessTokenUri())
                .body(formData)
                .retrieve()
                .toEntity(AuthToken.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            authToken = response.getBody();
        } else {
            throw new IllegalStateException("Unable to refresh token");
        }
    }

    private MultiValueMap<String, String> createFormData() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "password");
        formData.add("client_id", props.getClientId());
        formData.add("client_secret", props.getClientSecret());
        formData.add("username", props.getUsername());
        formData.add("password", props.getPassword());
        formData.add("scope", props.getScope());

        return formData;
    }

}
