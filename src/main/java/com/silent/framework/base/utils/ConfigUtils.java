package com.silent.framework.base.utils;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author lixin
 */
public class ConfigUtils {
	
	/**
	 * 获取某路径下带有某标识的文件夹 
	 * @param path
	 * @param index
	 */
	public static String getDirectoryByIndex(String path, String index){
		File file = new File(path);
		File [] childrenFiles = file.listFiles();
		String cmDirectory = "";
		if(ArrayUtils.isEmpty(childrenFiles)){
			return cmDirectory;
		}
		for(int i = 0; i < childrenFiles.length; i++){
			File childrenFile = childrenFiles[i];
			if(childrenFile.isDirectory() && childrenFile.getName().indexOf(index) != -1){
				if(cmDirectory.equals("")){
					cmDirectory = childrenFile.getName();
				}else{
					String tmpCmDirectoryVision = childrenFile.getName().split("-")[1];
					String cmDirectoryVision = cmDirectory.split("-")[1];
					cmDirectory = index + getNewVersion(tmpCmDirectoryVision, cmDirectoryVision);
				}
			}
		}
		return cmDirectory;
	}
	
	
	/**
	 * 判断是否为最新版本方法 将版本号根据.切分为int数组 比较
	 * @param compareVersion 本地版本号
	 * @param comparedVersion  线上版本号
	 * @return
	 */
	public static String getNewVersion(String compareVersion, String comparedVersion) {
		String newestVision = compareVersion;
		if (compareVersion.equals(comparedVersion)) {
			newestVision = comparedVersion;
			return newestVision;
		}
		String[] localArray = compareVersion.split("\\.");
		String[] onlineArray = comparedVersion.split("\\.");

		int length = localArray.length < onlineArray.length ? localArray.length: onlineArray.length;
		for (int i = 0; i < length; i++) {
			if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
				newestVision =  comparedVersion;
				break;
			} else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
				newestVision =  compareVersion;
				break;
			}
		}
		return newestVision;
	}
	
	public static void main(String[] args) {
		String clouderaAgentConfigPath="/opt/$1/etc/cloudera-scm-agent/";
		String cmDirectorypath = clouderaAgentConfigPath.split("\\$1")[0];
		System.out.println(cmDirectorypath);
		clouderaAgentConfigPath = clouderaAgentConfigPath.replace("$1", getDirectoryByIndex(cmDirectorypath, "cm-"));
		
		System.out.println(clouderaAgentConfigPath);
	}
}
