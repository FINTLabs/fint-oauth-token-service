package no.fint.oauth

import no.fint.oauth.testutils.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = TestApplication, properties = ['fint.oauth.json=classpath:auth.json', 'fint.oauth.enabled=true'])
class OAuthConfigJsonIntegrationSpec extends Specification {

    @Autowired
    OAuthConfig oAuthConfig

    def "OAuth Configuration is enabled"() {
        when:
        println(oAuthConfig.props())

        then:
        oAuthConfig.props().username == 'testusername'
    }
}
