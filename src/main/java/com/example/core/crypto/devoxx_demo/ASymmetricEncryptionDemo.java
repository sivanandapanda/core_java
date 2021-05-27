package com.example.core.crypto.devoxx_demo;

import javax.crypto.Cipher;
import java.security.KeyPairGenerator;

public class ASymmetricEncryptionDemo {

    public static void main(String[] args) throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        var keyPair = generator.generateKeyPair();
        //System.out.println("private key " + new String(keyPair.getPrivate().getEncoded()));
        //System.out.println("public key " + new String(keyPair.getPublic().getEncoded()));

        var text = "The lord of the rings has been read by many people".getBytes();

        var cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        var encrypted = cipher.doFinal(text);

        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
        var dencrypted = cipher.doFinal(encrypted);
        System.out.println(new String(dencrypted));
    }

}
