package no.fint.oauth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "fint.oauth.enabled", havingValue = "true")
public class TokenService {

    private static final String BEARER_TOKEN_TEMPLATE = "Bearer %s";
    private final RestClient oauthRestClient;
    private final OAuthTokenProps props;
    private final TokenInstance tokenInstance;

    @PostConstruct
    public void init() {
        if (ObjectUtils.isEmpty(props.getRequestUrl())) {
            log.info("No request-url configured, will not initialize access token");
        } else {
            refreshConnection(props.getRequestUrl());
        }
    }

    private void refreshConnection(String requestUrl) {
        ResponseEntity<Void> response = oauthRestClient.post()
                .uri(requestUrl)
                .headers(header -> header.add(
                        HttpHeaders.AUTHORIZATION, BEARER_TOKEN_TEMPLATE.formatted(tokenInstance.getAccessToken())
                ))
                .retrieve()
                .toBodilessEntity();
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Unable to get access token from %s. Status: %d", props.getRequestUrl(), response.getStatusCode().value()));
        }
    }

    public String getAccessToken(String requestUrl) {
        if (tokenInstance.isNull() || tokenInstance.hasExpired()) {
            tokenInstance.refreshToken();
            refreshConnection(requestUrl);
        }
        return tokenInstance.getAccessToken();
    }

    public String getAccessToken() {
        return getAccessToken(props.getRequestUrl());
    }

    public String getBearerToken() {
        return getBearerToken(props.getRequestUrl());
    }

    public String getBearerToken(String requestUrl) {
        String accessToken = getAccessToken(requestUrl);
        if (accessToken != null) {
            return String.format(BEARER_TOKEN_TEMPLATE, accessToken);
        }
        return null;
    }

}
