package no.fint.oauth

import no.fint.oauth.testutils.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class TokenServiceIntegrationSpec extends Specification {

    @Autowired(required = false)
    private TokenService tokenService

    @Autowired(required = false)
    private OAuthRestTemplateFactory factory

    @Autowired(required = false)
    private RestTemplate restTemplate

    def "Disable TokenService when fint.oauth.enabled is set to false"() {
        when:
        def disabled = (tokenService == null)

        then:
        disabled
    }

    def "Create OAuth2 rest template"() {
        when:
        def disabled = (factory == null)

        then:
        disabled
    }

    def "RestTemplate is valid even if fint.oauth.enabled=false"() {
        expect:
        restTemplate != null
    }

}
