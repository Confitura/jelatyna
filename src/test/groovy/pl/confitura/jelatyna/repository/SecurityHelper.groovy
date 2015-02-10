package pl.confitura.jelatyna.repository

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import pl.confitura.jelatyna.domain.Participant
import pl.confitura.jelatyna.domain.User
import pl.confitura.jelatyna.domain.UserPermission

import java.security.Principal


trait SecurityHelper {
    Principal logInAs(String email, ArrayList<UserPermission> permissions) {
        Participant participant = new Participant(email: email)
        User user = new User(participant: participant)


        def principal = new UsernamePasswordAuthenticationToken(user, "admin", permissions)
        SecurityContextHolder.getContext().setAuthentication(principal);
        return principal
    }

    Principal logInAsAdmin() { return logInAs("admin@admin.pl", [UserPermission.ADMIN]) }
}