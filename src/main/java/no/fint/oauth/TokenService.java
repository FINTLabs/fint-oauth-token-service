package no.fint.oauth;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@ConditionalOnProperty(name = "fint.oauth.enabled", havingValue = "true")
public class TokenService {

    private static final String BEARER_TOKEN_TEMPLATE = "Bearer %s";
    private final RestClient restClient = RestClient.builder().build();
    private final OAuthTokenProps props;
    private final MultiValueMap<String, String> formData;
    private AuthToken authToken;

    public TokenService(OAuthTokenProps props) {
        this.props = props;
        this.formData = createFormData();
    }

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(props.getRequestUrl())) {
            log.info("No request-url configured, will not initialize access token");
        } else {
            refreshToken(props.getRequestUrl());
        }
    }

    private void refreshToken(String requestUrl) {
        ResponseEntity<AuthToken> response = restClient.post()
                .uri(requestUrl)
                .body(formData)
                .retrieve()
                .toEntity(AuthToken.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Unable to get access token from %s. Status: %d", props.getRequestUrl(), response.getStatusCode().value()));
        } else {
            authToken = response.getBody();
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

    public String getAccessToken(String requestUrl) {
        if (authToken == null || tokenHasExpired(5)) {
            refreshToken(requestUrl);
        }
        return authToken.accessToken();
    }

    private boolean tokenHasExpired(int expiredTime) {
        return authToken.expiresIn() < expiredTime;
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
