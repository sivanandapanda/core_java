package com.example.core.crypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public class KeyStoreExample {

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {
        System.out.println("Default keystore ==> " + KeyStore.getDefaultType());
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        //KeyStore keyStore = KeyStore.getInstance("PKCS12");

        char[] keyStorePassword = "123abc".toCharArray();
        try(InputStream keyStoreData = new FileInputStream("keystore.ks")){
            keyStore.load(keyStoreData, keyStorePassword);
            //keyStore.load(null, keyStorePassword); //If you don't want to load any data into the KeyStore, just pass null

            char[] keyPassword = "789xyz".toCharArray();
            KeyStore.ProtectionParameter entryPassword =
                    new KeyStore.PasswordProtection(keyPassword);

            KeyStore.Entry keyEntry = keyStore.getEntry("keyAlias", entryPassword);

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)
                    keyStore.getEntry("keyAlias", entryPassword);
        }

        try (FileOutputStream keyStoreOutputStream = new FileOutputStream("data/keystore.ks")) {
            keyStore.store(keyStoreOutputStream, keyStorePassword);
        }
    }
}
