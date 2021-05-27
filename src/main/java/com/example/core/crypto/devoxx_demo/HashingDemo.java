package com.example.core.crypto.devoxx_demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingDemo {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String input = "The red fox jumps over blue dog";

        var messageDigest = MessageDigest.getInstance("SHA-256");
        var digest = messageDigest.digest(input.getBytes());
        System.out.println(new String(digest));
    }
}
