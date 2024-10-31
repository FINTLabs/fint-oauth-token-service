package no.fint.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@ConditionalOnProperty(name = OAuthTokenProps.ENABLE_OAUTH, havingValue = "true")
@Configuration
public class OAuthConfig {

    @Bean
    public OAuthTokenProps props() {
        return new OAuthTokenProps();
    }

    @Bean
    public TokenService tokenService(OAuthTokenProps props) {
        return new TokenService(props);
    }

}
