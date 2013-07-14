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
	
	
	public WebRobot(GetMethod getMethod) {
		this.httpClient = HttpUtil.getHttpClient();
		this.getMethod = getMethod;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	
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
