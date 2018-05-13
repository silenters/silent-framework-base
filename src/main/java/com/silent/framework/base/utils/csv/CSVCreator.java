package com.silent.framework.base.utils.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * CSV生成器
 * @author TanJin
 * @date 2018年4月11日
 */
public class CSVCreator {
	private static final String DEFAULT_SEPARATOR=",";
	private static final char LINE_BREAK = '\n';
	private String separator = DEFAULT_SEPARATOR;
	private List<String[]> rows = new ArrayList<String[]>();
	
	/**
	 * 生成CSV格式字符串
	 */
	public String toCSVString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows.size(); i++) {
			String [] row = rows.get(i);
			if(row == null){
				continue;
			}
			for (int j = 0; j < row.length; j++) {
				sb.append(row[j]);
				if(j != row.length - 1){
					sb.append(separator);
				}else{
					sb.append(LINE_BREAK);
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 保存到指定文件
	 * @param file
	 * @return
	 */
	public void saveToFile(File file) throws IOException{
		if(file.exists()){
			file.delete();
		}
		FileUtils.write(file, this.toCSVString(), "UTF-8");
	}
	
	/**
	 * 设置CSV头信息
	 * @param heads
	 * @return
	 */
	public void setHead(String [] heads){
		if(rows.isEmpty()){
			rows.add(heads);
		}else{
			rows.set(0, heads);
		}
	}
	
	/**
	 * 添加CSV信息
	 * @param row
	 * @return
	 */
	public void addRow(String [] row){
		if(rows.size() > 1){
			rows.add(row);
		}else{
			rows.add(null);
			rows.add(row);
		}
	}
	
	/**
	 * 获取数量
	 */
	public int size(){
		return rows.size();
	}

	/**
	 * 获取分隔符
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * 设置分隔符
	 * @param separator
	 * @return
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
}
