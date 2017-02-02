package com.fundin.utils.security;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class SymmetricEncryptionUtils {

	private SymmetricEncryptionUtils() {
		
	}
	
	private static final String DEFAULT_ENCODING = "utf-8";
	
	public static String encryptByAES(String str, String key) throws Exception {
        if (null == str || str.trim().length() < 1) {
            throw new Exception();
        }

        byte[] keybytes = hexToByte(key);

        SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted1 = str.getBytes(DEFAULT_ENCODING);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = byte2hex(original);
        return originalString;

    }
	
	public static String decryptByAES(String str, String key) throws Exception {
        if (null == str || str.trim().length() < 1) {
        	throw new Exception();
        }

        byte[] keybytes = hexToByte(key);

        SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] encrypted1 = hexToByte(str);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, DEFAULT_ENCODING);
    }
	
	public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) hs = hs + "0" + stmp;
            else hs = hs + stmp;

        }
        return hs.toUpperCase();
    }
	
	public static byte[] hexToByte(String s) throws IOException {
        int i = s.length() / 2;
        byte abyte0[] = new byte[i];
        int j = 0;
        if (s.length() % 2 != 0)
            throw new IOException("hexadecimal string with odd number of characters");
        for (int k = 0; k < i; k++) {
            char c = s.charAt(j++);
            int l = "0123456789abcdef0123456789ABCDEF".indexOf(c);
            if (l == -1)
                throw new IOException("hexadecimal string contains non hex character");
            int i1 = (l & 0xf) << 4;
            c = s.charAt(j++);
            l = "0123456789abcdef0123456789ABCDEF".indexOf(c);
            i1 += l & 0xf;
            abyte0[k] = (byte) i1;
        }
        return abyte0;
    }
	
	
	public static final String ENCRYPT_KEY_RESET = byte2hex("fundin2016fundin".getBytes());
	public static final String ENCRYPT_KEY_COOKIE = byte2hex("fundinfundin2016".getBytes());
	
	public static void main(String[] args) throws Exception {
    	System.out.println(encryptByAES("gavin1333", "EDA3D558B223AC4DCC3DB19675A7D324"));
    	System.out.println(decryptByAES("EEF93132D6C3A704264A475BF95F4FF0", 
    			"EDA3D558B223AC4DCC3DB19675A7D324"));
				
		Long userId = 1L;
		String encryptStr = SymmetricEncryptionUtils.encryptByAES(userId.toString(), ENCRYPT_KEY_RESET);
		String originalStr = SymmetricEncryptionUtils.decryptByAES(encryptStr, ENCRYPT_KEY_RESET);
		System.out.println(userId + " " + encryptStr + " " + originalStr);
    }
	
}
