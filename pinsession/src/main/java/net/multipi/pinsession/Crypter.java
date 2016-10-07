package net.multipi.pinsession;

import android.util.Base64;

import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Smmarat on 06.10.16.
 */

public class Crypter {

    private final static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    public static String encrypt(String data, String pass) throws Exception {
        Key key = pass2key(pass);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, getIv());
        byte[] bytes = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(bytes, Base64.URL_SAFE);
    }

    public static String decrypt(String data, String pass) throws Exception {
        Key key = pass2key(pass);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, getIv());
        byte[] bytes = cipher.doFinal(Base64.decode(data, Base64.URL_SAFE));
        return new String(bytes);
    }

    private static IvParameterSpec getIv() {
        byte[] iv = hexToByte("01f01f01f01f01f01f01f01f01f01f01");
        return new IvParameterSpec(iv);
    }

    private static Key pass2key(String pass) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] salt = hexToByte("4205c730b7afd0c048a9e9775ac4167e");
        KeySpec keyspec = new PBEKeySpec(pass.toCharArray(), salt, 1000, 128);
        SecretKey secretKey = factory.generateSecret(keyspec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    private static byte[] hexToByte(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    public static String byteToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length*2];
        int v;

        for(int j=0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v>>>4];
            hexChars[j*2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
