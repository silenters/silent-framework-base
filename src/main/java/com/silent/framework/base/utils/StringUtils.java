package com.silent.framework.base.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 字符串操作工具类
 * @author TanJin
 */
@SuppressWarnings("restriction")
public class StringUtils {

	/**
	 * 是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return str == null || str.isEmpty();
	}
	
	/**
	 * 是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return str != null && !str.isEmpty();
	}
	
	/**
	 * 字符串去除前后空格之后是否为空
	 * @param str
	 * @return
	 */
	public static boolean isTrimEmpty(String str){
		return str == null || str.length() == 0 || str.trim().isEmpty();
	}
	
	/**
	 * String转Integer
	 * @param str
	 * @param defaultValue 默认值
	 * @return
	 */
	public static int parseInt(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		} catch (NullPointerException e) {
			return defaultValue;
		}
	}

	/**
	 * String转Long
	 * @param str
	 * @param defaultValue 默认值
	 * @return
	 */
	public static long parseLong(String str, long defaultValue) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		} catch (NullPointerException e) {
			return defaultValue;
		}
	}

	/**
	 * String转Double
	 * @param str
	 * @param defaultValue 默认值
	 * @return
	 */
	public static double parseDouble(String str, double defaultValue) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		} catch (NullPointerException e) {
			return defaultValue;
		}
	}

	/**
	 * String转Boolean
	 * @param str
	 * @param defaultValue 默认值
	 * @return
	 */
	public static boolean parseBoolean(String background, boolean defaultValue) {
		try{
			return Boolean.parseBoolean(background);
		}catch(Exception e){
			return defaultValue;
		}
	}
	
	/**
	 * 生成指定长度的随机字符串
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		String randomStr = random(length, 71);
        return randomStr.toLowerCase();
    }
	
	 /**
     * Pseudo-random number generator object for use with randomString().
     * The Random class is not considered to be cryptographically secure, so
     * only use these random Strings for low to medium security applications.
     */
    private transient static Random randGen = new Random();

    /**
     * Array of numbers and letters of mixed case. Numbers appear in the list
     * twice so that there is a more equal chance that a number will be picked.
     * We can use the array to get a random number or letter by picking a random
     * array index.
     */
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    
    /**
     * 创建随机数字，默认每一位最大数不超过9
     * @param length
     * @return
     */
    public static String randomNumber(int length){
    	return random(length, 9);
    }
    
    /**
	 * 获取一个在min到max之间的随机数字，包括min、max
	 * @param min 最小值
	 * @param max 最大值
	 * @date 2017-1-10
	 */
	public static long randomNumber(int min, int max) {
		return Math.round(Math.random()*(max - min)) + min;
	}
    
	/**
	 * 生成随机字符串
	 * @param 
	 * @return
	 */
    private static String random(int length, int max){
    	if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(max)];
        }
        return new String(randBuffer);
    }
    
    /**
     * Replaces all instances of oldString with newString in string.
     *
     * @param string the String to search to perform replacements on.
     * @param oldString the String that should be replaced by newString.
     * @param newString the String that will replace all instances of oldString.
     * @return a String will all instances of oldString replaced by newString.
     */
    public static String replace(String string, String oldString, String newString) {
        if (string == null) {
            return null;
        }
        int i = 0;
        // Make sure that oldString appears at least once before doing any processing.
        if ((i = string.indexOf(oldString, i)) >= 0) {
            // Use char []'s, as they are more efficient to deal with.
            char[] string2 = string.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(string2.length);
            buf.append(string2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            // Replace all remaining instances of oldString with newString.
            while ((i = string.indexOf(oldString, i)) > 0) {
                buf.append(string2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(string2, j, string2.length - j);
            return buf.toString();
        }
        return string;
    }

    /**
     * Replaces all instances of oldString with newString in line with the
     * added feature that matches of newString in oldString ignore case.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replaceIgnoreCase(String line, String oldString,String newString) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * Replaces all instances of oldString with newString in line with the
     * added feature that matches of newString in oldString ignore case.
     * The count paramater is set to the number of replaces performed.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @param count     a value that will be updated with the number of replaces
     *                  performed.
     * @return a String will all instances of oldString replaced by newString
     */
    public static String replaceIgnoreCase(String line, String oldString,String newString, int[] count){
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            int counter = 1;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * Replaces all instances of oldString with newString in line.
     * The count Integer is updated with number of replaces.
     *
     * @param line the String to search to perform replacements on.
     * @param oldString the String that should be replaced by newString.
     * @param newString the String that will replace all instances of oldString.
     * @return a String will all instances of oldString replaced by newString.
     */
    public static String replace(String line, String oldString,String newString, int[] count) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            int counter = 1;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }
    
    
    /**
	 * 字节数组转换成16进制
	 * @param data
	 * @return
	 */
	public static String byteToHex(byte[] data){
		if(null == data || data.length <= 0){
			return "";
		}
		
		StringBuffer buffer = new StringBuffer();
		for(int b : data){
			String value = Integer.toHexString(b&0xFF);
			if(value.length() == 1){
				buffer.append("0");
			}
			buffer.append(value);
		}
		String str = buffer.toString();
		return str;
	}
	
	/**
	 * 十六进制字符串转byte数组
	 * @param hexString 十六进制字符串
	 * @date 2016-6-24
	 */
	public static byte[] hexStringToBytes(String hexString) {   
	    if (isEmpty(hexString)) {   
	        return null;   
	    }
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	} 
	
	/**
	 * char 转 byte
	 * @date 2016-6-24
	 */
	private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}
	
	/**
	 * 字符串转十六进制
	 * @author TanJin
	 * @date 2015-6-10
	 */
	public static String stringToHex(String content){
		if(isEmpty(content)){
			return null;
		}
		String[] array = content.split(",");
		StringBuffer buffer = new StringBuffer();
		for(String str : array){
			int i = Integer.parseInt(str);
			String value = Integer.toHexString(i & 0xFF);
			if(value.length() == 1){
				buffer.append("0");
			}
			buffer.append(value);
		}
		return buffer.toString();
	}

	/**
	 * 字符串去除重复
	 * 字符串格式：1,2,3,4,5....
	 * @date 2015-4-21
	 */
	public static String removeStringRepeat(String str) {
		if(isNotEmpty(str)) {
			String[] strArray = str.split(",");
			Set<String> tempSet = new HashSet<String>();
			int length = null != strArray ? strArray.length : 0;
			for(int i = 0; i < length; i++){
				if(isNotEmpty(strArray[i])){
					tempSet.add(strArray[i]);
				}
			}
			if(tempSet.size() > 0) {
				Iterator<String> iterator = tempSet.iterator();
				StringBuilder sb = new StringBuilder();
				String separator = "";
				while (iterator.hasNext()) {
					sb.append(separator);
					sb.append(iterator.next());
					separator = ",";
				}
				return sb.toString();
			}
		}
		return "";
	}
	
	/**
	 * 将字符串数组转换为int型list
	 * @date 2015-5-10
	 */
	public static List<Integer> arrayToList(String[] items) {
		List<Integer> list = new ArrayList<Integer>();
		String[] itemArray = convertArray(items);
		for(String item : itemArray){
			list.add(parseInt(item, 0));
		}
		return list;
	}
	
	/**
	 * 根据字符串获取字符串列表
	 * @param symbol 分隔符 如：, 、 @ 等
	 * @date 2015-8-7
	 */
	public static List<String> stringToList(String content, String symbol) {
		if(isEmpty(content)){
			return new ArrayList<String>();
		}
		String[] itemArray = content.split(symbol);
		List<String> list = new ArrayList<String>();
		for(String str : itemArray){
			list.add(str);
		}
		return list;
	}
	
	/**
	 * 字符串根据分隔符转换为int型list
	 * @param string  字符串
	 * @param symbol 分隔符   如@ # , . \ 等等
	 * @date 2015-11-6
	 */
	public static List<Integer> stringToIntegerList(String string, String symbol){
		if(isEmpty(string)){
			return new ArrayList<Integer>();
		}
		
		String[] itemArray = string.split(symbol);
		List<Integer> list = new ArrayList<Integer>();
		
		for(String str : itemArray){
			if(isNotEmpty(str)) {
				list.add(parseInt(str, 0));
			}
		}
		return list;
	}
	
	/**
	 * 将指定数组中字符串进行分割，获取新数组
	 * @date 2015-5-10
	 */
	public static String[] convertArray(String[] items) {
		if(null == items || items.length == 0){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for(String str : items){
			sb.append(str).append(",");
		}
		return sb.toString().split(",");
	}
	
	/**
	 * 将String字符串数组转换为String型list
	 * @date 2015-5-10
	 */
	public static List<String> convertStrList(String[] items) {
		List<String> list = new ArrayList<String>();
		String[] itemArray = convertArray(items);
		
		if(null == itemArray){
			return list;
		}
		for(String item : itemArray){
			list.add(item);
		}
		return list;
	}
	
	 public static Collection<String> stringToCollection(String string) {
        if (string == null || string.trim().length() == 0) {
            return Collections.emptyList();
        }
        Collection<String> collection = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(string, ",");
        while (tokens.hasMoreTokens()) {
            collection.add(tokens.nextToken().trim());
        }
        return collection;
     }
	
	/**
	 * 将字符数组转换为字符串
	 * @date 2015-6-7
	 */
	public static String arrayToString(String[] array) {
		if(null == array || array.length == 0){
			return null;
		}
		
		List<Integer> list = arrayToList(array);
		StringBuilder str = new StringBuilder();
		String separator = "";
		for(int value : list){
			str.append(separator);
			str.append(value);
			separator = ",";
		}
		return str.toString();
	}
	
	/**
	 * 判断 str2 是否是 str1的子串
	 * 若str1、str2有一个为空，则返回false
	 * @param str1  包含的字符串 
	 * @param  str2 被包含的字符串
	 * @return boolean
	 * @date 2015-12-9
	 */
	public static boolean isContains(String str1, String str2){
		if(isEmpty(str1) || isEmpty(str2)){
			return false;
		}
		return str1.contains(str2);
	}
	
	/**
	 * 添加分隔符
	 * @date 2015-9-29
	 */
	public static String addSeparator(String str){
		if(isEmpty(str)){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(",").append(str).append(",");
		return builder.toString();
	}

	/**
	 * 清除分隔符
	 * @date 2015-9-29
	 */
	public static String removeSeparator(String str){
		if(isEmpty(str)){
			return "";
		}
		String[] array = str.split(",");
		StringBuilder builder = new StringBuilder();
		for(String item : array){
			if(isNotEmpty(item)){
				builder.append(item).append(",");
			}
		}
		if(builder.length() > 0){
			builder.deleteCharAt(builder.length()-1);
		}
		return builder.toString();
	}
	
	/**
     * 将以Base64方式编码的字符串解码为byte数组
     * @param encodeStr 待解码的字符串
     * @return 解码后的byte数组
     * @throws IOException 
     * */
	public static byte[] decryptBase64(String encodeStr) throws IOException{
    	if(isEmpty(encodeStr)){
    		return null;
    	}
        byte[] bt = null;  
        BASE64Decoder decoder = new BASE64Decoder();  
        bt = decoder.decodeBuffer(encodeStr);
        return bt;
    }
    
    /**
     * 将以Base64方式编码的字符串解码为字符串
     * @param encodeStr 待解码的字符串
     * @return 解码后的字符串
     * @throws IOException 
     * */
    public static String decryptBase64ToString(String encodeStr) throws IOException{
    	byte[] bt = decryptBase64(encodeStr);
    	if(bt.length == 0){
    		return null;
    	}
    	return new String(bt);
    }
    
    /**
     * 将字符串以Base64方式加密
     * @param encodeStr 待解码的字符串
     * @return 解码后的字符串
     * @throws IOException 
     * */
	public static String encryptBase64(String str) throws IOException{
    	if(isEmpty(str)){
    		return null;
    	}
    	BASE64Encoder encoder = new BASE64Encoder();
    	String encoderStr = encoder.encodeBuffer(str.getBytes());
    	encoderStr = encoderStr.replaceAll("[\\s*\t\n\r]", ""); 
    	return encoderStr;       
    }
    
    /**
     * 清楚字符串格式
     * @date 2015-7-2
     */
    public static String cleanStringFormat(String str) {
    	String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式
		String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		String regEx_houhtml = "/[^>]+>"; // 定义HTML标签的正则表达式
		String regEx_blank = "\\s*|\t|\r|\n"; // 定义空格的正则表达式
		String regEx_spe = "\\&[^;]+;"; // 定义特殊符号的正则表达式
		String regEx_other = "&nbsp;";		//其他字符
		
		String cleanStr = str.replaceAll(regEx_script, "").replaceAll(regEx_style, "")
				.replaceAll(regEx_html, "").replaceAll(regEx_houhtml, "").replaceAll(regEx_blank, "")
				.replaceAll(regEx_spe, "").replaceAll(regEx_other, "");
		
    	return cleanStr;
    }
    
    /**
     * 判断字符
     * @date 2015-11-4
     */
    private static boolean check(String regex, String value){
    	if(null == value) {
    		return false;
    	}
		Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.find();
	}
    
    /**
     * 检查字符是否是整数、小数(小数点后仅一位),若是则返回true,否则返回false
     * @date 2016-6-24
     */
    public static boolean checkDecimal(String value) {
    	return check("^[0-9]+(.[0-9]{1})?$", value);
    }
    
    /**
	 * 检查密码是否合法
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(String password){
		if(password == null || password.length() < 6 || password.length() > 16){
			return false;
		}
		int a = check("[a-z]", password) ? 10 : 0;
		int b = check("[0-9]", password) ? 10 : 0;
		return a+b >= 10;
	}
	
	/**
	 * 检查是否是邮箱
	 * @param value
	 * @return
	 */
	public static boolean isEmail(String value){
		if(value == null){
			return false;
		}
		return check("^([a-z0-9A-Z]+[-|_|\\.]*)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", value);
	}
	
	/**
	 * 检查是否是手机号
	 * @param value
	 * @return
	 */
	public static boolean isMobile(String value){
		if(value==null){
			return false;
		}
		return check("^(13[0,1,2,3,4,5,6,7,8,9]|14[5,7]|15[0,1,2,3,4,5,6,7,8,9]|17[0]|18[0,2,5,6,7,8,9])\\d{8}$", value);
	}
	

	/**
	 * 通过-连接所有对象
	 * @param keys
	 * @date 2016-05-23
	 * @return
	 */
	public static String buildKey(Object... keys) {
		StringBuilder builder = new StringBuilder();
		for(Object key : keys) {
			builder.append(key);
			builder.append("-");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
}
