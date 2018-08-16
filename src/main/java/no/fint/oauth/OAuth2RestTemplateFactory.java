package no.fint.oauth;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.Collections;

public class OAuth2RestTemplateFactory {

    public static OAuth2RestTemplate create(OAuthTokenProps props) {
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

}
