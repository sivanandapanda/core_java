package com.example.core.crypto.devoxx_demo;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//Cipher block chaining mode, there will be no repeating encrypted characters
public class SymmetricEncryptionCBCDemo {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        var keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(192);
        var key = keyGenerator.generateKey();

        var input = "Devox!!".repeat(16).getBytes();

        var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        var secureRandom = SecureRandom.getInstance("SHA1PRNG");
        var random = new byte[16];
        secureRandom.nextBytes(random);
        var ivParameterSpec = new IvParameterSpec(random);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        var encryptedOutpt = cipher.doFinal(input);
        System.out.println(new String(encryptedOutpt));

        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        var dencryptedOutpt = cipher.doFinal(encryptedOutpt);
        System.out.println(new String(dencryptedOutpt));
    }
}
