package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import me.yongbo.robot.util.DbHelper;
import me.yongbo.robot.util.HttpUtil;

import org.apache.commons.httpclient.HttpClient;
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
		httpClient.executeMethod(getMethod);
		BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), "UTF-8"));
        String s; 
        StringBuilder sb = new StringBuilder("");
        while ((s = reader.readLine()) != null) {
            sb.append(s);
        }
        reader.close();
        getMethod.releaseConnection();
        System.out.println(sb.toString());
        return sb.toString(); 
	}
}
