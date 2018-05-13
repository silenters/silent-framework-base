package com.silent.framework.base.utils;

public class FillZeroUtils {

	/**
	 * 获取正值长度
	 * @param x
	 * @return
	 */
	public static int valueSize(long x) {
		long p = 10;
		for (int i = 1; i < 19; i++) {
			if (x < p)
				return i;
			p = 10 * p;
		}
		return 19;
	}
	
	/**
	 * 补零补齐两位
	 * @param sb
	 * @param num
	 */
	public static void fillTo2Digits(StringBuilder sb, long num){
		if(num < 10){
			sb.append(0);
		}
		sb.append(num);
	}
	
	/**
	 * 数字补零
	 * @param totalBits 总位数
	 * @param value     需补零的数值
	 * @return          补零后的字符串
	 */
	public static void fillZeroFormat(StringBuilder sb, int totalBits, long value){
		int size = FillZeroUtils.valueSize(value);
		
		if (size < totalBits) {
			for(int i =0; i < (totalBits - size); ++i){
				sb.append(0);
			}
		}
		sb.append(value);
	}
}
