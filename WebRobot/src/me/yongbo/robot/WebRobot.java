package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import me.yongbo.robot.util.HttpUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

public class WebRobot implements Runnable {
	
	protected HttpClient httpClient;
	protected GetMethod getMethod;
	
	//标志位，用于指示线程是否继续执行（如遇到错误，则停止运行）
	protected boolean doAgain = true;
	
	/**
	 * 构造函数
	 * @param getMethod 一个GetMethod实例
	 * */
	public WebRobot(GetMethod getMethod) {
		this.httpClient = HttpUtil.getHttpClient();
		this.getMethod = getMethod;
		
	}
	/**
	 * 构造函数
	 * */
	public WebRobot() {}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	/**
	 * 通过URL地址获取页面的html字符串
	 * @param url 目标地址
	 * */
	public String getResponseString(String url) throws Exception {
		getMethod.setURI(new URI(url));
		int status = httpClient.executeMethod(getMethod);
		if(status != HttpStatus.SC_OK){
			return null;
		}

        return getMethod.getResponseBodyAsString(); 
	}
	/**
	 * 发起http请求
	 * @param webUrl 请求连接地址
	 * */
	public String httpGet(String webUrl) throws Exception {
		URL url;
		URLConnection conn;
		StringBuilder sb = new StringBuilder();
		url = new URL(webUrl);
		conn = url.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bufReader = new BufferedReader(isr);
		String lineText;
		while ((lineText = bufReader.readLine()) != null) {
			sb.append(lineText);
		}
		return sb.toString();
	}
}
