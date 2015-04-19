package pl.confitura.jelatyna.user;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {

    private SecureRandom random = new SecureRandom();

    public String generate() {
        return new BigInteger(130, random).toString(32);
    }

    public String encrypt(String password) {
         return "encrypted" + password;
    }
}
