package com.silent.framework.base.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图片裁剪工具类
 * @author TanJin
 * @date 2015-11-16
 */
public class ImageCut {

	/**
	 * 图片切割
	 * 
	 * @param byte[]	原图数据
	 * @param x		目标切片坐标 X轴起点
	 * @param y		目标切片坐标 Y轴起点
	 * @param w		目标切片 宽度
	 * @param h		目标切片 高度
	 * @param maxWidth 
	 * @param maxHeight 
	 */

	public static byte[] cutImage(byte[] data, int x, int y, int w, int h, String maxWidth, String maxHeight)
			throws IOException {
		BufferedImage image = read(data);
		if(image.getType() <= 0){
			return null;
		}
		
		image = zoomInImages(image,maxWidth,maxHeight); // 图片按比例缩放

		if ((x + w) > image.getWidth()) {
			w = image.getWidth() - x;
		}
		if ((y + h) > image.getHeight()) {
			h = image.getHeight() - y;
		}

		BufferedImage subImage = image.getSubimage(x, y, w, h);

		ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("JPEG").next();
		ImageWriteParam params = imageWriter.getDefaultWriteParam();
		params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		params.setCompressionQuality(1.0f);
		params.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

		ColorModel colorModel = subImage.getColorModel();
		SampleModel sampleModel = colorModel.createCompatibleSampleModel(16, 16);
		
		ImageTypeSpecifier imageTypeSpecifier = new ImageTypeSpecifier(colorModel, sampleModel);
		params.setDestinationType(imageTypeSpecifier);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(bos);
		
		imageWriter.setOutput(imageOutputStream);
		IIOImage iIamge = new IIOImage(subImage, null, null);
		imageWriter.write(null, iIamge, params);
		imageWriter.dispose();
		imageOutputStream.close();
		return bos.toByteArray();
	}

	/**
	 * 读取图片
	 * @date 2015-11-16
	 */
	public static BufferedImage read(byte[] data) 
			throws FileNotFoundException,IOException {
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try {
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(bais);
			while (iterator.hasNext() && img == null) {
				ImageReader reader = iterator.next();
				reader.setInput(bais);
				img = reader.read(0);
			}
		} finally {
			bais.close();
		}
		return img;
	}

	/**
	 * 
	 * @Title zoomInImages
	 * @author TanJin
	 * @param maxHeight 
	 * @param maxWidth 
	 * @Description 图片按比例缩放
	 * @param
	 * @return BufferedImage
	 */
	public static BufferedImage zoomInImages(BufferedImage originalImage, String maxWidth, String maxHeight) {
		
		float widthTimes = (float) (originalImage.getWidth() / Float.parseFloat(maxWidth));
		float heightTimes = (float) (originalImage.getHeight() / Float.parseFloat(maxHeight));
		float times = 0;
		if (widthTimes >= heightTimes) {
			times = widthTimes;
		} else if (widthTimes < heightTimes) { 
			times = heightTimes;
		}

		if (times > 1) {
			int newWidth = (int) (originalImage.getWidth() / times);
			int newHeight = (int) (originalImage.getHeight() / times);

			BufferedImage newImage = new BufferedImage(newWidth, newHeight,
					originalImage.getType());
			Graphics g = newImage.getGraphics();
			g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
			g.dispose();
			return newImage;
		}
		return originalImage;
	}
}
