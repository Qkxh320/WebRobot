package me.yongbo.robot;

import java.util.ArrayList;
import java.util.List;

import me.yongbo.robot.bean.FunnyObj;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class FunnyDataRobot extends DatabaseRobot {
	
	public void doWork(FunnyObj obj){
		List<NameValuePair> nvs = new ArrayList<NameValuePair>();
		nvs.add(new BasicNameValuePair("from", obj.getFrom()));
		nvs.add(new BasicNameValuePair("content", obj.getContent()));
		nvs.add(new BasicNameValuePair("pic", obj.getImgUrl()));
		nvs.add(new BasicNameValuePair("user_id", obj.getId()));
		nvs.add(new BasicNameValuePair("user_name", obj.getUserName()));
		nvs.add(new BasicNameValuePair("createtime", obj.getCreateTime()));
		postDataToServer(nvs);
	}
}
