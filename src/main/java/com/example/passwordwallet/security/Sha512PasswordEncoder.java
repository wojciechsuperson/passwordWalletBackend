package com.example.passwordwallet.security;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class Sha512PasswordEncoder {

    private static final Random RANDOM = new SecureRandom();
    /**
     * To calculate digest you can type:
     * String SHA512Hash = calculateSHA512(pepper+salt+text);
     *
     * @param text salt+text to encode
     * @return
     */
    public String encode(String text) {
        try {
            //get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            //calculate message digest of the input string - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param rawPassword salt+rawPass
     * @param encodedPassword pass from database
     * @return
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else if (encodedPassword != null && encodedPassword.length() != 0) {
            return encodedPassword.equals(encode(rawPassword));
        } else {
            return false;
        }
    }

    public String getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
