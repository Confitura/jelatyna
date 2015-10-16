package pl.confitura.jelatyna.user

import groovy.json.JsonBuilder
import pl.confitura.jelatyna.user.domain.Role
import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.User

class UserBuilder {
    def person = new Person(firstName: "John", lastName: "Smith", email: "john@smith.invalid")
    def user = new User(person: person).addRole(Role.ADMIN)


    def id(id) {
        user.id = id
        person.id = id
    }

    def twitter(twitter) {
        user.twitter = twitter
    }

    def code(code) {
        user.code = code
    }

    def bio(bio) {
        user.bio = bio
    }

    def password(password) {
        user.password = password
    }

    def name(String name) {
        println(name)
        person.firstName = name.split(" ")[0];
        person.lastName = name.split(" ")[1];
    }

    def email(email) {
        person.email = email;
    }

    def firstName(String firstName) {
        person.firstName = firstName;
    }

    def lastName(lastName) {
        person.lastName = lastName;
    }

    def token(token) {
        user.token = token;
    }


    static User aUser(block) {
        def builder = new UserBuilder();
        builder.with block
        return builder.user
    }

    static String aUserAsJson(block) {
        return new JsonBuilder(aUser(block)).toString();
    }

    static String aPersonAsJson(block) {
        return new JsonBuilder(aUser(block).person).toString();
    }

}
