package com.wwish.ganalytics.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by gaocaili on 2017/7/11.
 */

public class AES {
    public AES() {
    }

    public static String encrypt(String input, String key) {
        byte[] crypted = null;

        try {
            SecretKeySpec e = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, e);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return new String(Base64Utils.encodeMessage(crypted));
    }

    public static String decrypt(String input, String key) {
        byte[] output = null;

        try {
            SecretKeySpec e = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, e);
            output = cipher.doFinal(Base64Utils.decodeMessage(input));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return new String(output);
    }
}
