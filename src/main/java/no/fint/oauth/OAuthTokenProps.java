package no.fint.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OAuthTokenProps {

    static final String ENABLE_OAUTH = "fint.oauth.enabled";

    @Value("${fint.oauth.username:}")
    private String username;

    @Value("${fint.oauth.password:}")
    private String password;

    @Value("${fint.oauth.access-token-uri:}")
    private String accessTokenUri;

    @Value("${fint.oauth.client-id:}")
    private String clientId;

    @Value("${fint.oauth.client-secret:}")
    private String clientSecret;

    @Value("${fint.oauth.request-url:}")
    private String requestUrl;

    @Value("${fint.oauth.scope:}")
    private String scope;

    @Value("${fint.oauth.grant-type:password}")
    private String grantType;
}
