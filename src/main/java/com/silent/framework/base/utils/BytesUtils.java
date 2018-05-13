package com.silent.framework.base.utils;

import java.nio.charset.Charset;

public class BytesUtils {
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	public static byte[] toBytes(int val) {
		byte[] b = new byte[4];
		for (int i = 3; i > 0; i--) {
			b[i] = (byte) val;
			val >>>= 8;
		}
		b[0] = (byte) val;
		return b;
	}

	public static byte[] toBytes(long val) {
		byte[] b = new byte[8];
		for (int i = 7; i > 0; i--) {
			b[i] = (byte) val;
			val >>>= 8;
		}
		b[0] = (byte) val;
		return b;
	}

	public static byte[] toBytes(String s) {
		return s.getBytes(UTF8_CHARSET);
	}
}
