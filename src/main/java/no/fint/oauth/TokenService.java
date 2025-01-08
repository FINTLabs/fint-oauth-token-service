package no.fint.oauth;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static no.fint.oauth.TokenClient.BEARER_TOKEN_TEMPLATE;

@Slf4j
@Service
@ConditionalOnProperty(value = "fint.oauth.enabled", havingValue = "true")
public class TokenService {

    private final OAuthTokenProps props;
    private final TokenInstance tokenInstance;
    private final TokenClient tokenClient;

    @Autowired
    public TokenService(@Qualifier("oauthRestClient") RestClient restClient, OAuthTokenProps props) {
        this.props = props;
        this.tokenClient = new TokenClient(restClient, props);
        this.tokenInstance = new TokenInstance(tokenClient, props);
    }

    public TokenService(@Qualifier("oauthWebClient") WebClient webClient, OAuthTokenProps props) {
        this.props = props;
        this.tokenClient = new TokenClient(webClient, props);
        this.tokenInstance = new TokenInstance(tokenClient, props);
    }

    @PostConstruct
    public void init() {
        if (ObjectUtils.isEmpty(props.getRequestUrl())) {
            log.info("No request-url configured, will not initialize access token");
        } else {
            refreshConnection(props.getRequestUrl());
        }
    }

    private void refreshConnection(String requestUrl) {
        ResponseEntity<Void> response = tokenClient.refresh(requestUrl, tokenInstance.getAccessToken());
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(String.format("Unable to get access token from %s. Status: %d", props.getRequestUrl(), response.getStatusCode().value()));
        }
    }

    public String getAccessToken(String requestUrl) {
        if (tokenInstance.isNull() || tokenInstance.hasExpired()) {
            log.info("Refreshing Token...");
            tokenInstance.refreshToken();
        }
        log.info("Giving existing token!");
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
