package no.fint.oauth

import no.fint.oauth.testutils.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class TokenServiceIntegrationSpec extends Specification {

    @Autowired(required = false)
    private TokenService tokenService

    @Autowired(required = false)
    private OAuthRestTemplateFactory factory

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

}
