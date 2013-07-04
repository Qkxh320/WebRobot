package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import me.yongbo.robot.util.HttpUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class Meitu91Robot extends WebRobot {
	private final static String HOST = "www.91meitu.net";
	private final static String REFERER = "http://www.91meitu.net/";
	private final static String POINT_URL = "http://www.91meitu.net/img-item/get-next?1&lastid=%1$d";
	
	private int lastId;
	private HttpClient httpClient;
	private GetMethod getMethod;
	public Meitu91Robot(){
		this(1);
	}
	
	public Meitu91Robot(int lastId){
		this.lastId = lastId;
		this.httpClient = HttpUtil.getHttpClient();
		this.getMethod = HttpUtil.getHttpGet(String.format(POINT_URL, lastId), REFERER, HOST);
	}
	
	public String doWork() throws Exception {
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
