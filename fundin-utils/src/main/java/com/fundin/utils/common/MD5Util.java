package com.fundin.utils.common;

import java.security.MessageDigest;

public final class MD5Util {
    
    private final static String[] strDigests = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigests[iD1] + strDigests[iD2];
    }

    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byteToString(md.digest(input.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException("MD5Util MD5 error, input: " + input, ex);
        }
    }

}