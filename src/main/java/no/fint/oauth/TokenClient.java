package no.fint.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@ConditionalOnProperty(value = "fint.oauth.enabled", havingValue = "true")
public class TokenClient {

    public static final String BEARER_TOKEN_TEMPLATE = "Bearer %s";

    private final RestClient restClient;
    private final WebClient webClient;
    private final MultiValueMap<String, String> formData;
    private final OAuthTokenProps props;

    public TokenClient(WebClient webClient, OAuthTokenProps props) {
        this.restClient = null;
        this.webClient = webClient;
        this.props = props;
        this.formData = createFormData();
    }

    @Autowired
    public TokenClient(@Qualifier("oauthRestClient") RestClient restClient, OAuthTokenProps props) {
        this.restClient = restClient;
        this.webClient = null;
        this.props = props;
        this.formData = createFormData();
    }

    public ResponseEntity<AuthToken> getAuthToken() {
        if (restClient != null) {
            return restClient.post()
                    .uri(props.getAccessTokenUri())
                    .body(formData)
                    .retrieve()
                    .toEntity(AuthToken.class);
        }

        if (webClient != null) {
            return webClient.post()
                    .uri(props.getAccessTokenUri())
                    .bodyValue(formData)
                    .retrieve()
                    .toEntity(AuthToken.class)
                    .block();
        }

        throw new IllegalStateException("No client is configured.");
    }

    public ResponseEntity<Void> refresh(String requestUrl, String accessToken) {
        if (restClient != null) {
            return restClient.get()
                    .uri(requestUrl)
                    .headers(header -> header.add(
                            HttpHeaders.AUTHORIZATION, BEARER_TOKEN_TEMPLATE.formatted(accessToken)
                    ))
                    .retrieve()
                    .toBodilessEntity();
        }

        if (webClient != null) {
            return webClient.post()
                    .uri(requestUrl)
                    .headers(header -> header.add(
                            HttpHeaders.AUTHORIZATION, BEARER_TOKEN_TEMPLATE.formatted(accessToken)
                    ))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }

        throw new IllegalStateException("No client is configured.");
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
