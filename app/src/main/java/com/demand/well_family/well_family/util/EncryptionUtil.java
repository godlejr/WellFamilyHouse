package com.demand.well_family.well_family.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public class EncryptionUtil {
    public static String encryptSHA256(String password) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes(), 0, password.length());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return String.format("%064x", new java.math.BigInteger(1, messageDigest.digest()));
    }
}
