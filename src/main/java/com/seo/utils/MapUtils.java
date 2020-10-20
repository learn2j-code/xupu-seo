package com.seo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;

/**
 * 调用高德  地理/逆地理编码
 * @author Javen
 * 2016年4月3日
 */
public class MapUtils {
	static Log log = Log.getLog(MapUtils.class);
	/* 高德配置资源 */
	private static final Prop prop = PropKit.use("map.properties");
	private final static String MAP_URL= prop.get("url");
	/* 高德配置详情 */
	private static final String MAP_OUTPUT = prop.get("output");
	private static final String MAP_KEY = prop.get("key");
	private static final String MAP_SKEY = prop.get("skey");
	

	public static String getLocation(String address) {
		String inputline = "";
		StringBuffer sb = new StringBuffer(MAP_URL+"?");
		sb.append("address="+address);
		sb.append("&output="+MAP_OUTPUT);
		sb.append("&key="+MAP_KEY);
		//String newStr = new String(content.getBytes(), "GBK");  
		//sb.append("&content="+URLEncoder.encode(content));
		// 创建url对象
		try {
			// 打开url连接
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");

			// 发送
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			// 返回发送结果
			inputline = in.readLine();

			// 返回结果为‘100’ 发送成功
			System.out.println(inputline);
			log.error("查询高德地图返回结果："+inputline);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputline;
		
	}
}
