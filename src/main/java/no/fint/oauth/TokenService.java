package no.fint.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.annotation.PostConstruct;

public class TokenService {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    private OAuthTokenProps props;

    @PostConstruct
    public void init() {
        refreshToken();
    }

    private void refreshToken() {
        ResponseEntity<Void> response = restTemplate.getForEntity(props.getRequestUrl(), Void.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Unable to get access token from %s. Status: %d", props.getRequestUrl(), response.getStatusCodeValue()));
        }
    }

    public String getAccessToken() {
        OAuth2AccessToken accessToken = restTemplate.getAccessToken();
        if (accessToken.getExpiresIn() > 5) {
            return accessToken.getValue();
        } else {
            refreshToken();
            return restTemplate.getAccessToken().getValue();
        }
    }

}
