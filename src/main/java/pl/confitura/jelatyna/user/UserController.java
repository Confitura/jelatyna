package pl.confitura.jelatyna.user;


import lombok.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@RestController("userController")
@RequestMapping("/api/user")
public class UserController {

	@RequestMapping("/current")
	@PreAuthorize("isAuthenticated()")
	public CurrentUserResponse current(Authentication authentication) {
		return CurrentUserResponse.of((UserDetails) authentication.getPrincipal());
	}

	@Value
	static class CurrentUserResponse{

		String username;
		Collection<String> roles;

		static CurrentUserResponse of(UserDetails principal) {
			Collection<String> roles = principal.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.collect(toList());

			return new CurrentUserResponse(principal.getUsername(), roles);
		}
	}
}
