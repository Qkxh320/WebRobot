package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class DatabaseRobot {
	
	public void postDataToServer(List<NameValuePair> nvs) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost("http://localhost:58167/api/add.php");
		try {
			// 设置表单提交编码为UTF-8
			try {
				httpost.setEntity(new UrlEncodedFormEntity(nvs, HTTP.UTF_8));
				HttpResponse response = httpclient.execute(httpost);
				HttpEntity entity = response.getEntity();
				InputStreamReader isr = new InputStreamReader(entity.getContent());
				BufferedReader bufReader = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String lineText = null;
				while ((lineText = bufReader.readLine()) != null) {
					sb.append(lineText);
				}
				System.out.println("response code: "
						+ response.getStatusLine() + " response:" + sb.toString());
				EntityUtils.consume(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
}
