package no.fint.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.Collections;

@Slf4j
@ConditionalOnProperty(name = OAuthTokenProps.ENABLE_OAUTH, havingValue = "true")
@Configuration
public class OAuthConfig {

    @Bean
    public OAuthTokenProps props() {
        return new OAuthTokenProps();
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate() {
        OAuthTokenProps props = props();
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername(props.getUsername());
        resourceDetails.setPassword(props.getPassword());
        resourceDetails.setAccessTokenUri(props.getAccessTokenUri());
        resourceDetails.setClientId(props.getClientId());
        resourceDetails.setClientSecret(props.getClientSecret());
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Collections.singletonList(props.getScope()));
        return new OAuth2RestTemplate(resourceDetails);
    }

    @Bean
    public TokenService tokenService() {
        return new TokenService();
    }

}
