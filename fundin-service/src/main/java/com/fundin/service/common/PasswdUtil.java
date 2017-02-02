package com.fundin.service.common;

import com.fundin.utils.common.Encodes;
import com.fundin.utils.security.Digests;

public class PasswdUtil {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	
	public static String generateSalt() {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		return Encodes.encodeHex(salt);
	}
	
	public static String encryptPasswd(String salt, String passwd) {
		byte[] hashPassword = Digests.sha1(passwd.getBytes(), Encodes.decodeHex(salt), HASH_INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}
	

}
