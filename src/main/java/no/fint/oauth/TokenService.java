package no.fint.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Slf4j
public class TokenService {

    private static final String BEARER_TOKEN_TEMPLATE = "Bearer %s";

    @Autowired
    @Qualifier("fintOauthRestTemplate")
    private OAuth2RestTemplate restTemplate;

    @Autowired
    private OAuthTokenProps props;

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(props.getRequestUrl())) {
            log.info("No request-url configured, will not initialize access token");
        } else {
            refreshToken(props.getRequestUrl());
        }
    }

    private void refreshToken(String requestUrl) {
        ResponseEntity<Void> response = restTemplate.getForEntity(requestUrl, Void.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Unable to get access token from %s. Status: %d", props.getRequestUrl(), response.getStatusCodeValue()));
        }
    }

    public String getAccessToken(String requestUrl) {
        OAuth2AccessToken accessToken = restTemplate.getAccessToken();
        if (accessToken.getExpiresIn() > 5) {
            return accessToken.getValue();
        } else {
            refreshToken(requestUrl);
            return restTemplate.getAccessToken().getValue();
        }
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
