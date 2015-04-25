package pl.confitura.jelatyna.user;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {


    private final PasswordEncoder passwordEncoder;

    private SecureRandom random = new SecureRandom();

    @Autowired
    public TokenGenerator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String generate() {
        return new BigInteger(130, random).toString(32);
    }

    public String encrypt(String password) {
         return passwordEncoder.encode(password);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }
}
