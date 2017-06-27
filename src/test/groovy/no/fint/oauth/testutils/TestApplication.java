package no.fint.oauth.testutils;

import no.fint.oauth.OAuthConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(OAuthConfig.class)
@SpringBootApplication
public class TestApplication {
}
