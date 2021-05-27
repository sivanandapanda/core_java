package com.example.core.crypto.devoxx_demo;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class DigitalSignatureDemo {

    public static void main(String[] args) throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        var keyPair = generator.generateKeyPair();
        //System.out.println("private key " + new String(keyPair.getPrivate().getEncoded()));
        //System.out.println("public key " + new String(keyPair.getPublic().getEncoded()));

        var text = "Java is the best!!!".getBytes();

        var signatureAlgo = Signature.getInstance("SHA256WithRSA");
        signatureAlgo.initSign(keyPair.getPrivate());
        signatureAlgo.update(text);
        var signature = signatureAlgo.sign();

        var verifySignatureAlgo = Signature.getInstance("SHA256WithRSA");
        verifySignatureAlgo.initVerify(keyPair.getPublic());
        verifySignatureAlgo.update(text);
        var matches = verifySignatureAlgo.verify(signature);
        System.out.println("Signature matches: " + matches);
    }

}
