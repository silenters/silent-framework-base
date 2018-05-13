package com.silent.framework.base.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


/**
 * 地图、 地理位置工具类
 * @author TanJin
 * @date 2015-9-14
 */
public class MapUtil {
	private static Logger logger = LoggerFactory.getLogger(MapUtil.class);

	//将GPS坐标转换为百度坐标
		private static String gps_conversion_baidu_url = 
				"http://api.map.baidu.com/ag/coord/convert?from=2&to=4&x=LATITUDE&y=LONGITUDE";
		
	//根据坐标获取具体位置信息
	private static String get_location_url = 
			"http://api.map.baidu.com/geocoder/v2/?ak=AK&location=LATITUDE,LONGITUDE&output=json&pois=1";
	
	 //百度地图API调用密匙AK
  	public static final String BAIDU_AK = "b47nAVuyeVOBt2HZhemqAGpt";
	
	/**
     * 获取位置信息
     * @param latitude 纬度
     * @param longitude	经度
	 * @throws Exception 
     * @date 2015-9-2
     */
    public static JSONObject getLocation(String latitude, String longitude) throws Exception {
    	if(StringUtils.isEmpty(latitude) || StringUtils.isEmpty(longitude)){
    		return null;
    	}
    	
    	Map<String, String> coordsMap = getBaiduCoords(latitude, longitude);
    	
    	//根据百度地图坐标系中的经纬度获取具体的地理位置信息
    	String getLocationUrl = get_location_url.replace("AK", BAIDU_AK).replace("LATITUDE", coordsMap.get("x")).replace("LONGITUDE", coordsMap.get("y")); 
    	JSONObject locationJson = HttpClientUtils.postJson(getLocationUrl, null, new HashMap<String, String>());
    	logger.info("location json : {}", locationJson);
    	return locationJson;
    }
    
    /**
     * 获取百度地图坐标
     * @param latitude  纬度
     * @param longitude  经度
     * @throws Exception 
     * @date 2015-10-20
     */
    private static Map<String, String> getBaiduCoords(String latitude, String longitude) throws Exception{
    	//将经纬度转换为百度地图坐标系中的经纬度
    	String conversionBaiduUrl = gps_conversion_baidu_url.replace("LATITUDE", latitude).replace("LONGITUDE", longitude);
    	
    	JSONObject jsonObject = HttpClientUtils.postJson(conversionBaiduUrl, null, new HashMap<String, String>());
    	logger.info("baidu latitude longitude json : {}", jsonObject);
    	
    	String baiduLatitude = "";
    	String baiduLongitude = "";
		try {
			baiduLatitude = StringUtils.decryptBase64ToString(jsonObject.getString("x"));
			baiduLongitude = StringUtils.decryptBase64ToString(jsonObject.getString("y"));
		} catch (IOException e) {
			logger.error("", e);  
		}
		logger.info("baidu latitude : {},  longitude : {}", baiduLatitude, baiduLongitude);
    	
		Map<String, String> map = new HashMap<String, String>();
		map.put("x", baiduLatitude);
		map.put("y", baiduLongitude);
    	
    	return map;
    	
    }
}
