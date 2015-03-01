package pl.confitura.jelatyna.common

import spock.lang.Specification


class TokenGeneratorSpec extends Specification {
    def "should generate token"() {
        given:
          def generator = new TokenGenerator()

        expect:
          def token = generator.generate()
          println token
          token != null


    }
}
