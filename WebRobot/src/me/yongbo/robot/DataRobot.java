package me.yongbo.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import me.yongbo.robot.bean.ArticleObj;
import me.yongbo.robot.bean.FunnyObj;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class DataRobot extends DatabaseRobot {
	//public static final String addfunny_url = "http://api.wakao.me/api/addFunny.php";
	public static final String ADD_API_URL = "http://apiforandroid.duapp.com/%1$s/add";
	// 线程池
	private static ExecutorService pool;
	public void addFunnyData(FunnyObj obj){
		List<NameValuePair> nvs = new ArrayList<NameValuePair>();
		nvs.add(new BasicNameValuePair("from", obj.getFrom()));
		nvs.add(new BasicNameValuePair("content", obj.getContent()));
		nvs.add(new BasicNameValuePair("pic", obj.getImgUrl()));
		nvs.add(new BasicNameValuePair("user_id", obj.getId()));
		nvs.add(new BasicNameValuePair("user_name", obj.getUserName()));
		nvs.add(new BasicNameValuePair("createtime", obj.getCreateTime()));
		postDataToServer(nvs, String.format(ADD_API_URL, "funny"));
	}
	
	public void addArticleData(ArticleObj obj) {
		List<NameValuePair> nvs = new ArrayList<NameValuePair>();
		nvs.add(new BasicNameValuePair("from", obj.getFrom()));
		nvs.add(new BasicNameValuePair("weixin_account", obj.getAuthor()));
		nvs.add(new BasicNameValuePair("content", obj.getContent()));
		nvs.add(new BasicNameValuePair("title", obj.getTitle()));
		nvs.add(new BasicNameValuePair("intro_words", obj.getIntro()));
		nvs.add(new BasicNameValuePair("pic", obj.getPic()));
		nvs.add(new BasicNameValuePair("user_id", "1"));
		nvs.add(new BasicNameValuePair("user_name", "爱吃鱼的猫"));
		nvs.add(new BasicNameValuePair("createtime", obj.getCreatetime()));
		postDataToServer(nvs, String.format(ADD_API_URL, "article"));
	}
}
