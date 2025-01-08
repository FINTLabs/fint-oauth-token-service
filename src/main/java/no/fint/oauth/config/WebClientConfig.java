package no.fint.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("oauthWebClient")
    public WebClient oauthRestClient() {
        return WebClient.create();
    }

}
