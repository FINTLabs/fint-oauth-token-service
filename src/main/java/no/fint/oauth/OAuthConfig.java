package no.fint.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Configuration
public class OAuthConfig {

    @Bean
    @ConditionalOnProperty(name = OAuthTokenProps.ENABLE_OAUTH, havingValue = "true")
    public OAuthTokenProps props() {
        return new OAuthTokenProps();
    }

    @Bean
    @ConditionalOnProperty(name = OAuthTokenProps.ENABLE_OAUTH, havingValue = "true")
    public OAuth2RestTemplate oauth2RestTemplate() {
        OAuthTokenProps props = props();
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername(props.getUsername());
        resourceDetails.setPassword(props.getPassword());
        resourceDetails.setAccessTokenUri(props.getAccessTokenUri());
        resourceDetails.setClientId(props.getClientId());
        resourceDetails.setClientSecret(props.getClientSecret());
        resourceDetails.setGrantType(props.getGrantType());
        resourceDetails.setScope(Collections.singletonList(props.getScope()));
        return new OAuth2RestTemplate(resourceDetails);
    }

    @Bean
    @ConditionalOnProperty(name = OAuthTokenProps.ENABLE_OAUTH, havingValue = "true")
    public OAuthRestTemplateFactory oAuthRestTemplateFactory() {
        return new OAuthRestTemplateFactory();
    }

    @Bean
    @ConditionalOnProperty(name = OAuthTokenProps.ENABLE_OAUTH, havingValue = "true")
    public TokenService tokenService() {
        return new TokenService();
    }

    @Bean
    @ConditionalOnProperty(name = OAuthTokenProps.ENABLE_OAUTH, matchIfMissing = true, havingValue = "false")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        log.debug("OAuth disabled, loading standard RestTemplate");
        return builder.build();
    }

}
