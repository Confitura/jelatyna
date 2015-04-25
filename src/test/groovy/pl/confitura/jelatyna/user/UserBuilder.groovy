package pl.confitura.jelatyna.user

import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.User

class UserBuilder {
    def person = new Person(firstName: "John", lastName: "Smith", email: "john@smith.invalid")

    def token = null

    def token(String token) {
        this.token = token
    }

    static def aUser(block) {
        def builder = new UserBuilder();
        builder.with block
        return builder.build()
    }

    def build() {

        return new User(person: person.setToken(token))

    }
}
