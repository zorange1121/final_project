package com.example.mqttdemo.util;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class CryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static String encrypt(String data, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64URLSafeString(encryptedData);
    }

}
