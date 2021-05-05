package com.example.core.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class Summary {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Hardcoded Key
        /*byte[] keyBytes   = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        String algorithm  = "RawBytes";
        SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);*/

        //Symmetric key generator
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 256;
        keyGenerator.init(keyBitSize, secureRandom);

        SecretKey secretKey = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        //Asymmetric key pair generator, public/private key
        /*SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        keyPairGenerator.initialize(256, secureRandom);

        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());*/

        byte[] plainText  = "abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.UTF_8);
        byte[] cipherText = cipher.doFinal(plainText);

        System.out.println("CipherTest => " + new String(cipherText));

        /*MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digest = messageDigest.digest(plainText);

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keyPair.getPublic());
        byte[] macBytes = mac.doFinal(plainText);*/

        /*Signature signature = Signature.getInstance("SHA256WithDSA");
        signature.initSign(keyPair.getPrivate(), secureRandom);
        signature.update(plainText);
        byte[] digitalSignature = signature.sign();


        Signature signature2 = Signature.getInstance("SHA256WithDSA");
        signature2.initVerify(keyPair.getPublic());
        signature2.update(plainText);
        boolean verified = signature2.verify(digitalSignature);

        System.out.println("verified = " + verified);

        System.out.println("Signature verified? => " + verified);*/
    }
}