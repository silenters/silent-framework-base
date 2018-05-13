package com.silent.framework.base.utils.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class ZipUncompressUtils {
	
	public static List<ZipUncompressBean> uncompress(byte[] data) throws IOException {
		List<ZipUncompressBean> resultList = new ArrayList<ZipUncompressBean>();
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		ZipInputStream zis = new ZipInputStream(is);
		try {
			ZipEntry entry = zis.getNextEntry();
			while(entry != null){
				if(!entry.isDirectory()){
					ZipUncompressBean bean = new ZipUncompressBean();
					bean.setName(entry.getName());
					byte [] buffer = new byte [4096];
					ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
					int len;
		            while ((len = zis.read(buffer)) >= 0) {
		            	arrayOutputStream.write(buffer, 0, len);
		            }
					bean.setContent(arrayOutputStream.toByteArray());
					arrayOutputStream.close();
					resultList.add(bean);
				}
				zis.closeEntry();
				entry = zis.getNextEntry();
			}
			return resultList;
		} finally {
			IOUtils.closeQuietly(zis);
		}
	}
	
}
