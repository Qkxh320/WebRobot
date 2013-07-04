package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import me.yongbo.bean.Referer;
import me.yongbo.robot.util.HttpUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class Meitu91Robot {
	
	public String doWork() throws Exception{
		HttpClient httpClient = HttpUtil.getHttpClient();
		GetMethod get = HttpUtil.getHttpGet("http://www.91meitu.net/img-item/get-next?1&lastid=50", Referer.Referer_91Meitu);
		httpClient.executeMethod(get);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"));
        String s; // 依次循环，至到读的值为空
        StringBuilder sb = new StringBuilder();
        while ((s = reader.readLine()) != null) {
            sb.append(s);
        }
        reader.close();
        get.releaseConnection();
        return s;
	}
}
