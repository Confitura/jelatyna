package pl.confitura.jelatyna.user

import groovy.json.JsonBuilder
import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.Role
import pl.confitura.jelatyna.user.domain.User

class UserBuilder {
    private final Person person = new Person(firstName: "John", lastName: "Smith", email: "john@smith.invalid")

    private final User user = new User(person: person).addRole(Role.ADMIN)

    void id(String id) {
        user.id = id
        person.id = id
    }

    void twitter(twitter) {
        user.twitter = twitter
    }

    void code(code) {
        user.code = code
    }

    void bio(bio) {
        user.bio = bio
    }

    void password(password) {
        user.password = password
    }

    void name(String name) {
        person.firstName = name.split(" ")[0]
        person.lastName = name.split(" ")[1]
    }

    void email(email) {
        person.email = email
    }

    void firstName(String firstName) {
        person.firstName = firstName
    }

    void lastName(lastName) {
        person.lastName = lastName
    }

    void token(token) {
        user.token = token
    }

    static User aUser(block) {
        UserBuilder builder = new UserBuilder()
        builder.with block
        return builder.user
    }

    static String aUserAsJson(block) {
        return new JsonBuilder(aUser(block)).toString()
    }

    static String aPersonAsJson(block) {
        return new JsonBuilder(aUser(block).person).toString()
    }

}
