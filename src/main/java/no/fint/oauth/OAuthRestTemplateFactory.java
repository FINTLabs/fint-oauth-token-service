package no.fint.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.Collections;

public class OAuthRestTemplateFactory {

    @Autowired
    private OAuthTokenProps props;

    public OAuth2RestTemplate create(String username, String password, String clientId, String clientSecret) {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        resourceDetails.setAccessTokenUri(props.getAccessTokenUri());
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(clientSecret);
        resourceDetails.setGrantType(props.getGrantType());
        resourceDetails.setScope(Collections.singletonList(props.getScope()));
        return new OAuth2RestTemplate(resourceDetails);
    }

}
