package com.silent.framework.base.utils;

import java.security.MessageDigest;

public class MD5 {
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	private static ThreadLocal<MessageDigest> threadLocal=new ThreadLocal<MessageDigest>();
	
	public static String md5(String str) {
		try {
			MessageDigest md = threadLocal.get();
			if(md == null){
				md = MessageDigest.getInstance("MD5");
				threadLocal.set(md);
			}
			md.reset();
			md.update(str.getBytes("utf-8"));
			byte[] bs = md.digest();
			char[] chars = encodeHex(bs);
			return new String(chars);
		} catch (Exception e) {
			throw new RuntimeException("md5 ,error", e);
		}
	}

	private static char[] encodeHex(final byte[] data) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & data[i]];
		}
		return out;
	}
}
