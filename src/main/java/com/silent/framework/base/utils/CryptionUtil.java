package com.silent.framework.base.utils;

/**
 * 加解密工具类
 * @author xuliang
 */
public class CryptionUtil {
	
	/**
	 * 加解密byte数组
	 * @param data
	 */
	public static void enDecryption(byte[] data){
		byte[] key = new byte[]{'y', 'm', (byte) 0xEA, 0x31, 0x34, 'r', 'n', 0x21, 0x44, 0x01, 'B', 'Y', 'R', 'Y', 0x77, 0x71};
		int index = 0, dataLen = data.length, keyLen = key.length;
		key[8] = (byte) (((byte)(dataLen % 255)) ^ key[8]);
		if(dataLen >= keyLen){
			key[2] = (byte)(key[2] + dataLen + key[8]);
		}
		for(int i = 0; i < dataLen; i++){
			index = i % keyLen;
			data[i] = (byte)(data[i] ^ key[index]);
		}
	}

}
