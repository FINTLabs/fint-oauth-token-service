package no.fint.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Getter
@Setter
@ToString
@Configuration
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthTokenProps {

    @Value("${fint.oauth.username:}")
    @JsonProperty
    private String username;

    @Value("${fint.oauth.password:}")
    @JsonProperty
    private String password;

    @Value("${fint.oauth.access-token-uri:}")
    @JsonProperty("idpUri")
    private String accessTokenUri;

    @Value("${fint.oauth.client-id:}")
    @JsonProperty
    private String clientId;

    @Value("${fint.oauth.client-secret:}")
    @JsonProperty("openIdSecret")
    private String clientSecret;

    @Value("${fint.oauth.request-url:}")
    @JsonIgnore
    private String requestUrl;

    @Value("${fint.oauth.scope:}")
    @JsonProperty
    private String scope;

    @Value("${fint.oauth.grant-type:password}")
    @JsonProperty
    private String grantType;

    @Value("${fint.oauth.json:}")
    @JsonIgnore
    private Resource jsonConfiguration;

    @PostConstruct
    void init() throws IOException {
        if (jsonConfiguration == null || !jsonConfiguration.isReadable())
            return;
        new ObjectMapper().readerForUpdating(this).readValue(jsonConfiguration.getInputStream());
    }
}
