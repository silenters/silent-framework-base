package com.silent.framework.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * HTTP Client工具类
 * @author TanJin
 */
public class HttpClientUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	/**
	 * HTTP POST请求并返回指定Class的对象
	 * @param url 请求URL
	 * @param request 请求数据对象
	 * @param responseClazz 返回数据Class
	 * @return
	 */
	public static <T> T postJavaObject(String url, Object request, Class<T> responseClazz) throws Exception {
		return postJavaObject(url, request, null, responseClazz);
	}
	
	/**
	 * HTTP POST请求并返回指定Class的对象
	 * @param url 请求URL
	 * @param request 请求数据对象
	 * @param headerMap 头信息Map
	 * @param responseClazz 返回数据Class
	 * @return
	 */
	public static <T> T postJavaObject(String url, Object request, Map<String, String> headerMap, Class<T> responseClazz) throws Exception {
		JSONObject requestJson = (JSONObject) JSONObject.toJSON(request);
		JSONObject responseJson = postJson(url, requestJson.toJSONString().getBytes(), headerMap);
		T t = JSON.toJavaObject(responseJson, responseClazz);
		return t;
	}
	
	/**
	 * HTTP POST请求并返回JSON格式数据
	 * @param url 请求URL
	 * @param request 请求数据
	 * @param headerMap 头信息Map
	 * @return
	 */
	public static JSONObject postJson(String url, byte[] request, Map<String, String> headerMap) throws Exception {
		HttpEntity entity = new ByteArrayEntity(request);
		byte[] result = post(url, entity, headerMap);
		String data = new String(result);
		return JSONObject.parseObject(data);
	}
	
	/**
	 * HTTP POST请求并返回JSON格式数据
	 * @param url 请求URL
	 * @param reqEntity 请求实体
	 * @param headers 头信息
	 * @return
	 */
	public static JSONObject postJson(String url, HttpEntity reqEntity, Header[] headers) throws Exception {
		byte[] result = post(url, reqEntity, headers);
		String data = new String(result);
		return JSONObject.parseObject(data);
	}
	
	/**
	 * HTTP GET请求并返回指定Class的对象
	 * @param url 请求URL
	 * @param headerMap 头信息Map
	 * @param responseClazz 返回数据Class
	 * @return
	 */
	public static <T> T getJavaObject(String url, Map<String, String> headerMap, Class<T> responseClazz) throws Exception {
		JSONObject responseJson = getJson(url, headerMap);
		T t = JSON.toJavaObject(responseJson, responseClazz);
		return t;
	}
	
	/**
	 * HTTP GET请求并返回JSON格式数据
	 * @param url 请求URL
	 * @param headers 头信息
	 * @return
	 */
	public static JSONObject getJson(String url, Header[] headers) throws Exception {
		byte[] result = get(url, headers);
		String data = new String(result);
		return JSONObject.parseObject(data);
	}
	
	/**
	 * HTTP GET请求并返回JSON格式数据
	 * @param url 请求URL
	 * @param headerMap 头信息Map
	 * @return
	 */
	public static JSONObject getJson(String url, Map<String, String> headerMap) throws Exception {
		byte[] result = get(url, headerMap);
		String data = new String(result);
		return JSONObject.parseObject(data);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 请求URL
	 * @param reqEntity 请求实体
	 * @return
	 */
	public static byte[] post(String url, HttpEntity reqEntity) throws Exception {
		Map<String, String> headerMap = new HashMap<String, String>();
		return post(url, reqEntity, headerMap);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 请求URL
	 * @param reqEntity 请求实体
	 * @param headers 头信息
	 * @return
	 */
	public static byte[] post(String url, HttpEntity reqEntity, Header[] headers) throws Exception {
		CloseableHttpResponse response = null;
		long time = System.currentTimeMillis();
		try {
			logger.info("[HttpClientUtils] [POST] [URL:{}]", url);
			CloseableHttpClient client = createHttpClient(url);
			HttpPost request = new HttpPost(url);
			if(headers != null && headers.length > 0) {
				request.setHeaders(headers);
			}
			request.setConfig(RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(10000).build());
			request.setEntity(reqEntity);
			response = client.execute(request, new BasicHttpContext());
			int status = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == status || HttpStatus.SC_PARTIAL_CONTENT == status) {
				return EntityUtils.toByteArray(response.getEntity());
			} else {
				throw new IOException("receive code:" + status);
			}
		} finally {
			logger.info("[HttpClientUtils] [POST] [POST END] [url:{}] [time:{}]", url, System.currentTimeMillis() - time);
			if(response != null) {
				response.close();
			}
		}
	}
	
	/**
	 * HTTP POST请求
	 * @param url 请求URL
	 * @param reqEntity 请求实体
	 * @param headerMap 头信息Map
	 * @return
	 */
	public static byte[] post(String url, HttpEntity reqEntity, Map<String, String> headerMap) throws Exception {
		CloseableHttpResponse response = null;
		long time = System.currentTimeMillis();
		try {
			logger.info("[HttpClientUtils] [POST] [URL:{}]", url);
			CloseableHttpClient client = createHttpClient(url);
			HttpPost request = new HttpPost(url);
			if(null != headerMap && headerMap.size() > 0) {
				for (String key : headerMap.keySet()) {
					request.setHeader(key, headerMap.get(key));
				}
			}
			request.setConfig(RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(10000).build());
			request.setEntity(reqEntity);
			response = client.execute(request, new BasicHttpContext());
			int status = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == status || HttpStatus.SC_PARTIAL_CONTENT == status) {
				return EntityUtils.toByteArray(response.getEntity());
			} else {
				throw new IOException("receive code:" + status);
			}
		} finally {
			logger.info("[HttpClientUtils] [POST] [POST END] [url:{}] [time:{}]", url, System.currentTimeMillis() - time);
			if(response != null) {
				response.close();
			}
		}
	}
	
	/**
	 * HTTP GET请求
	 * @param url 请求URL
	 * @param headers 头信息
	 * @return
	 */
	public static byte[] get(String url, Header[] headers) throws Exception {
		CloseableHttpResponse response = null;
		long time = System.currentTimeMillis();
		try {
			logger.info("[HttpClientUtils] [GET] [URL:{}]", url);
			CloseableHttpClient client = createHttpClient(url);
			HttpGet request = new HttpGet(url);
			if(headers != null) {
				request.setHeaders(headers);
			}
			response = client.execute(request, new BasicHttpContext());
			int status = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == status || HttpStatus.SC_PARTIAL_CONTENT == status) {
				return EntityUtils.toByteArray(response.getEntity());
			} else {
				throw new IOException("receive code:" + status);
			}
		} finally {
			logger.info("[HttpClientUtils] [GET] [GET END] [URL:{}] [Time:{}]", url, System.currentTimeMillis() - time);
			if(response != null) {
				response.close();
			}
		}
	}
	
	/**
	 * HTTP GET请求
	 * @param url 请求URL
	 * @param headers 头信息Map
	 * @return
	 */
	public static byte[] get(String url, Map<String, String> headerMap) throws Exception {
		CloseableHttpResponse response = null;
		long time = System.currentTimeMillis();
		try {
			logger.info("[HttpClientUtils] [GET] [URL:{}]", url);
			CloseableHttpClient client = createHttpClient(url);
			HttpGet request = new HttpGet(url);
			if(null != headerMap && headerMap.size() > 0) {
				for (String key : headerMap.keySet()) {
					request.setHeader(key, headerMap.get(key));
				}
			}
			response = client.execute(request, new BasicHttpContext());
			int status = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == status || HttpStatus.SC_PARTIAL_CONTENT == status) {
				return EntityUtils.toByteArray(response.getEntity());
			} else {
				throw new IOException("receive code:" + status);
			}
		} finally {
			logger.info("[HttpClientUtils] [GET] [GET END] [URL:{}] [Time:{}]", url, System.currentTimeMillis() - time);
			if(response != null) {
				response.close();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private static CloseableHttpClient createHttpClient(String url) throws Exception {
		if(url.startsWith("https://")) {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();

			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, 
					new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setSslcontext(sslContext).build();
		}
		return HttpClients.createDefault();
	}
	
	/**
	 * 带基本验证（Basic authentication）的get请求
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static String requestWithAuth(String url, String username, String password,String requestType,String param) {  
        StringBuffer buffer = new StringBuffer();  
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader =null;
        InputStream inputStream = null;
        HttpURLConnection connection =null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();  
            String encoded =  Base64.encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8)); 
            connection.setRequestProperty("Authorization", "Basic "+encoded);
            connection.setDoOutput(true);  
            connection.setDoInput(true);  
            connection.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            connection.setRequestMethod(requestType);  
            connection.connect();
            //发送post请求参数
            if(requestType.equals("POST")){
	            connection.getOutputStream().write(param.getBytes());
	            connection.getOutputStream().flush();
            }
            // 将返回的输入流转换成字符串  
            inputStream = connection.getInputStream();  
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
        } catch (ConnectException ce) {  
            logger.error("server connection timed out.{}",ce);  
        } catch (Exception e) {  
        	logger.error("https request error:{}", e);  
        }finally{
        	 try {
        		 if(inputStream!=null){
        			 inputStream.close(); 
        		 }
        		 if(inputStreamReader!=null){
        			 inputStreamReader.close(); 
        		 }
        		 if(bufferedReader!=null){
        			 bufferedReader.close();
        		 }
        		 if(connection!=null){
        			 connection.disconnect();
        		 }
			} catch (IOException e) {
				logger.error("", e);  
			}  
        }
        return buffer.toString();  
	}
}