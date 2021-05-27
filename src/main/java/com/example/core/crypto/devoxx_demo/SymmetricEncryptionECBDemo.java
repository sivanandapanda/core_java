package com.example.core.crypto.devoxx_demo;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

//there will be repeating encrypted characters. the input will be broken to a block of 8 bits and encrypted.
// So in below example of text "devoxx!!" will broken down with same test and encryption will have same text repeated.
public class SymmetricEncryptionECBDemo {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        var keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(192);
        var key = keyGenerator.generateKey();

        var input = "Devox!!".repeat(16).getBytes();

        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        var encryptedOutpt = cipher.doFinal(input);
        System.out.println(new String(encryptedOutpt));

        cipher.init(Cipher.DECRYPT_MODE, key);
        var dencryptedOutpt = cipher.doFinal(encryptedOutpt);
        System.out.println(new String(dencryptedOutpt));
    }
}
