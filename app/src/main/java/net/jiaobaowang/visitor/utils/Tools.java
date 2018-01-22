package net.jiaobaowang.visitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;

import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.common.VisitorConstant;
import net.jiaobaowang.visitor.entity.ShakeHandData;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by rocka on 2018/1/18.
 */

public class Tools {
    public static String RSAEncrypt(String s, ShakeHandData shakeHandData) throws Exception {
        RSAPublicKey key = getPublicKey(shakeHandData);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(s.getBytes());
        return bytesToHex(encryptedBytes);
    }

    private static RSAPublicKey getPublicKey(ShakeHandData shakeHandData) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger modulus = new BigInteger(shakeHandData.getModulus(), 16);
        BigInteger exponent = new BigInteger(shakeHandData.getExponent(), 16);
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
        return pub;
    }

    public static String getAppId(Context context) {
        return context.getPackageName();
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getSign(TreeMap<String, String> map) throws InvalidKeyException, NoSuchAlgorithmException {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String key : map.keySet()) {
            arrayList.add(key + "=" + map.get(key));
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (i < arrayList.size() - 1) {
                stringBuilder.append("&");
            }
        }
        String a = stringBuilder.toString();
        return hmacSha1Encrypt(a, VisitorConstant.SIGN_KEY);
    }

    private static String hmacSha1Encrypt(String value, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        String type = "HmacSHA1";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static int getSchoolId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt(VisitorConfig.VISIT_LOCAL_SCHOOL_ID, 0);
    }
}
