package com.gsafety.starscream.capi.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class HttpRequest {
	
	public static String sendJSON(String path, String json, String method) throws Exception{
		method = (null!=method ? method : "GET");
		byte[] data = json.getBytes();
		HttpURLConnection conn;
		OutputStream outStream = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(method.toUpperCase());
			conn.setConnectTimeout(5 * 1000);
			conn.setDoOutput(true);//如果通过post提交数据，必须设置允许对外输出数据
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			outStream = conn.getOutputStream();
			outStream.write(data);
			outStream.flush();
			return IOUtils.toString(conn.getInputStream(), "UTF-8");
		} finally {
			if(null!=outStream){
				outStream.close();
			}
		}
	}

	public static String sendGetRequest(String path, Map<String, Object> params, String enc) throws Exception{
		StringBuilder sb = new StringBuilder(path);
		if(null!=params && !params.isEmpty()){
			sb.append('?');
			for(Map.Entry<String, Object> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=')
				.append(URLEncoder.encode(String.valueOf(entry.getValue()), enc)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		HttpURLConnection conn;
		URL url = new URL(sb.toString());
		conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		return IOUtils.toString(conn.getInputStream(), enc);
	}
	
	public static String sendPostRequest(String path, Map<String, Object> params, String enc) throws Exception{
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, Object> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=').append(URLEncoder.encode(String.valueOf(entry.getValue()), enc)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		byte[] entitydata = sb.toString().getBytes();//得到实体的二进制数据
		OutputStream outStream = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5 * 1000);
			conn.setDoOutput(true);//如果通过post提交数据，必须设置允许对外输出数据
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
			outStream = conn.getOutputStream();
			outStream.write(entitydata);
			outStream.flush();
			return IOUtils.toString(conn.getInputStream(), enc);
		} finally {
			if(null!=outStream){
				outStream.close();
			}
		}
	}
}
