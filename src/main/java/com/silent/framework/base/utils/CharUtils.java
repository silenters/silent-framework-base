package com.silent.framework.base.utils;

import java.io.UnsupportedEncodingException;

/**
 * 字符工具类
 * @author TanJin
 * @date 2015-10-28
 */
public class CharUtils {
	
	/**
	 * 对文本进行UTF8编码
	 * @date 2015-10-28
	 */
	public static String Utf8Enode(String text){
		if(StringUtils.isEmpty(text)){
			return "";
		}
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < text.length(); i++){
			 char c = text.charAt(i);
			 if(c >= 0 && c <= 255){
				 result.append(c);
			 }else{
				 byte[] b = new byte[0];
				 try {
					 b = Character.toString(c).getBytes("UTF-8");
				} catch (Exception e) {
				
				}
				for(int j = 0; j < b.length; j++){
					int k = b[j];
					if(k < 0){
						k += 256;
					}
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			 }
		}
		 return result.toString();
	}
	
	/**
	 * 对文本进行UTF-8解码
	 * @date 2015-10-28
	 */
	public static String utf8Decode(String text){
		String result = "";
        int p = 0;
        if(null != text && text.length() > 0){
        	text = text.toLowerCase();
        	p = text.indexOf("%e");
        	if(p == -1){
        		return text;
        	}
        	while (p != -1) {
				result += text.substring(0, p);
				text = text.substring(p, text.length());
				if(text == "" || text.length() < 9){
					return result;
				}
				result += CodeToWord(text.substring(0, 9));
				text = text.substring(9, text.length());
				p = text.indexOf("%e");
			}
        }
        return result + text;
	}
	
	/**
	 * 用于判断文本是否是经过UTF8编码
	 * @date 2015-10-28
	 */
	public boolean isUtf8Url(String text) {
		text = text.toLowerCase();
		int p = text.indexOf("%");
		if (p != -1 && text.length() - p > 9) {
			text = text.substring(p, p + 9);
		}
		return Utf8codeCheck(text);
	}
	
	/**
	 * 将UTF8编码转换为word
	 * @date 2015-10-28
	 */
	private static String CodeToWord(String text) {
		String result;
		if (Utf8codeCheck(text)) {
			byte[] code = new byte[3];
			code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
			code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
			code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
			try {
				result = new String(code, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				result = null;
			}
		} else {
			result = text;
		}
		return result;
	}
	
	/**
	 * 检测字符是否是UTF8编码
	 * @date 2015-10-28
	 */
	private static boolean Utf8codeCheck(String text) {
		String sign = "";
		if (text.startsWith("%e"))
			for (int p = 0; p != -1;) {
				p = text.indexOf("%", p);
				if (p != -1)
					p++;
				sign += p;
			}
		return sign.equals("147-1");
	}
}
