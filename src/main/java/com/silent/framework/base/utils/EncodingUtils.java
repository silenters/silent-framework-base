package com.silent.framework.base.utils;

import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class EncodingUtils {

	// Constants used by escapeHTMLTags
	static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
	static final char[] AMP_ENCODE = "&amp;".toCharArray();
	static final char[] LT_ENCODE = "&lt;".toCharArray();
	static final char[] GT_ENCODE = "&gt;".toCharArray();
	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p/>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 *
	 * @param bytes an array of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static String encodeHex(byte[] bytes) {
	    StringBuilder buf = new StringBuilder(bytes.length * 2);
	    int i;
	
	    for (i = 0; i < bytes.length; i++) {
	        if (((int)bytes[i] & 0xff) < 0x10) {
	            buf.append("0");
	        }
	        buf.append(Long.toString((int)bytes[i] & 0xff, 16));
	    }
	    return buf.toString();
	}
	
	/**
	 * 转成16进制，并且每两个字节中间加上空格
	 * @param bytes
	 * @return
	 */
	public static String encodeHexWithSpace(byte[] bytes) {
	    StringBuilder buf = new StringBuilder(bytes.length * 2);
	    int i;
	
	    for (i = 0; i < bytes.length; i++) {
	        if (((int)bytes[i] & 0xff) < 0x10) {
	            buf.append("0");
	        }
	        buf.append(Long.toString((int)bytes[i] & 0xff, 16));
	        buf.append(" ");
	    }
	    return buf.toString();
	}
	
	
	/**
	 * Turns a hex encoded string into a byte array. It is specifically meant
	 * to "reverse" the toHex(byte[]) method.
	 *
	 * @param hex a hex encoded String to transform into a byte array.
	 * @return a byte array representing the hex String[
	 */
	public static byte[] decodeHex(String hex) {
	    char[] chars = hex.toCharArray();
	    byte[] bytes = new byte[chars.length / 2];
	    int byteCount = 0;
	    for (int i = 0; i < chars.length; i += 2) {
	        int newByte = 0x00;
	        newByte |= EncodingUtils.hexCharToByte(chars[i]);
	        newByte <<= 4;
	        newByte |= EncodingUtils.hexCharToByte(chars[i + 1]);
	        bytes[byteCount] = (byte)newByte;
	        byteCount++;
	    }
	    return bytes;
	}
	/**
	 * Returns the the byte value of a hexadecmical char (0-f). It's assumed
	 * that the hexidecimal chars are lower case as appropriate.
	 *
	 * @param ch a hexedicmal character (0-f)
	 * @return the byte value of the character (0x00-0x0F)
	 */
	static byte hexCharToByte(char ch) {
	    switch (ch) {
	        case '0':
	            return 0x00;
	        case '1':
	            return 0x01;
	        case '2':
	            return 0x02;
	        case '3':
	            return 0x03;
	        case '4':
	            return 0x04;
	        case '5':
	            return 0x05;
	        case '6':
	            return 0x06;
	        case '7':
	            return 0x07;
	        case '8':
	            return 0x08;
	        case '9':
	            return 0x09;
	        case 'a':
	            return 0x0A;
	        case 'b':
	            return 0x0B;
	        case 'c':
	            return 0x0C;
	        case 'd':
	            return 0x0D;
	        case 'e':
	            return 0x0E;
	        case 'f':
	            return 0x0F;
	    }
	    return 0x00;
	}
	/**
	 * Converts a line of text into an array of lower case words using a
	 * BreakIterator.wordInstance().<p>
	 *
	 * This method is under the Jive Open Source Software License and was
	 * written by Mark Imbriaco.
	 *
	 * @param text a String of text to convert into an array of words
	 * @return text broken up into an array of words.
	 */
	public static String[] toLowerCaseWordArray(String text) {
	    if (text == null || text.length() == 0) {
	        return new String[0];
	    }
	
	    List<String> wordList = new ArrayList<String>();
	    BreakIterator boundary = BreakIterator.getWordInstance();
	    boundary.setText(text);
	    int start = 0;
	
	    for (int end = boundary.next(); end != BreakIterator.DONE;
	         start = end, end = boundary.next()) {
	        String tmp = text.substring(start, end).trim();
	        // Remove characters that are not needed.
	        tmp = StringUtils.replace(tmp, "+", "");
	        tmp = StringUtils.replace(tmp, "/", "");
	        tmp = StringUtils.replace(tmp, "\\", "");
	        tmp = StringUtils.replace(tmp, "#", "");
	        tmp = StringUtils.replace(tmp, "*", "");
	        tmp = StringUtils.replace(tmp, ")", "");
	        tmp = StringUtils.replace(tmp, "(", "");
	        tmp = StringUtils.replace(tmp, "&", "");
	        if (tmp.length() > 0) {
	            wordList.add(tmp);
	        }
	    }
	    return wordList.toArray(new String[wordList.size()]);
	}
	/**
	 * Escapes all necessary characters in the String so that it can be used in SQL
	 *
	 * @param string the string to escape.
	 * @return the string with appropriate characters escaped.
	 */
	public static String escapeForSQL(String string) {
	    if (string == null) {
	        return null;
	    }
	    else if (string.length() == 0) {
	        return string;
	    }
	
	    char ch;
	    char[] input = string.toCharArray();
	    int i = 0;
	    int last = 0;
	    int len = input.length;
	    StringBuilder out = null;
	    for (; i < len; i++) {
	        ch = input[i];
	
	        if (ch == '\'') {
	            if (out == null) {
	                 out = new StringBuilder(len + 2);
	            }
	            if (i > last) {
	                out.append(input, last, i - last);
	            }
	            last = i + 1;
	            out.append('\'').append('\'');
	        }
	    }
	
	    if (out == null) {
	        return string;
	    }
	    else if (i > last) {
	        out.append(input, last, i - last);
	    }
	
	    return out.toString();
	}
	/**
	 * ת�巽��
	 * @param string
	 * @return
	 */
	public static String escapeForXML(String string) {
		if (string == null) {
			return null;
		}
		char ch;
		int i;
		int last = 0;
		char[] input = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 13 / 10));
		for (i = 0; i < len; i++) {
			ch = input[i];
			if (ch > '>') {
			} else if (ch < (byte) 32) {
				if (ch == (byte) 10 || ch == (byte) 13) {
				} else {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
				}
			} else if (ch == '<') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '>') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(GT_ENCODE);
			} else if (ch == '&') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				// Do nothing if the string is of the form &#235; (unicode
				// value)
				if (!(len > i + 5 && input[i + 1] == '#'
						&& Character.isDigit(input[i + 2])
						&& Character.isDigit(input[i + 3])
						&& Character.isDigit(input[i + 4]) && input[i + 5] == ';')) {
					last = i + 1;
					out.append(AMP_ENCODE);
				}
			} else if (ch == '"') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(QUOTE_ENCODE);
			} else if (ch == (byte) 0 || ch == (byte) 1) {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
			}
		}
		if (last == 0) {
			return string;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}
	/**
	 * Unescapes the String by converting XML escape sequences back into normal
	 * characters.
	 *
	 * @param string the string to unescape.
	 * @return the string with appropriate characters unescaped.
	 */
	public static String unescapeFromXML(String string) {
	    string = StringUtils.replace(string, "&lt;", "<");
	    string = StringUtils.replace(string, "&gt;", ">");
	    string = StringUtils.replace(string, "&quot;", "\"");
	    return StringUtils.replace(string, "&amp;", "&");
	}
	/**
	 * convert byte[] to String with default encoding 
	 * @param bytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getUTFString(byte[] bytes) throws UnsupportedEncodingException{
		if(bytes==null){
			return null;
		}
		return new String(bytes,0,bytes.length,"UTF-8");
	}
	/**
	 * convert byte[] to String with default encoding 
	 * @param bytes
	 * @param offset
	 * @param len
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getUTFString(byte[] bytes,int offset,int len) throws UnsupportedEncodingException{
		if(bytes==null){
			return null;
		}
		return new String(bytes,offset,len,"UTF-8");
	}
	/**
	 * convert String to byte[] with default encoding
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getUTFBytes(String str) throws UnsupportedEncodingException{
		if(StringUtils.isEmpty(str)){
			return null;
		}
		return str.getBytes("UTF-8");
	}
}
