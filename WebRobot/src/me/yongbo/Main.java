package me.yongbo;


import java.util.Map;

import com.baidu.bae.api.factory.BaeFactory;
import com.baidu.bae.api.fetchurl.BaeFetchurl;

import me.yongbo.robot.HuabanRobot;
import me.yongbo.robot.Meitu91Robot;
import me.yongbo.robot.QiubaiRobot;
import me.yongbo.robot.util.PropertieUtil;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		/**
		 * 使用示例
		 * */
		//new Thread(new QiubaiRobot(1, 2, "late")).start(); //抓取最新下所有数据
		
		
		/**
		 * 使用示例
		 * */
		//http://meitu91.b0.upaiyun.com/
		//new Thread(new Meitu91Robot(1, 100, true)).start(); //抓取最新下数据

		/**
		 * 使用示例
		 * */
		//HuabanRobot hr = new HuabanRobot("94060078", "pets", false);
		//hr.setDebugMode(true);
		//new Thread(hr).start(); //抓取宠物下数据
		
		
		/*****1. 从工厂中获取fetchurl对象*****/
  		BaeFetchurl fetch = BaeFactory.getBaeFetchurl();

		/***************2.设置请求参数****************/
		//设置请求头参数
		fetch.setHeader("Expect", "");
		//设置最大重定向次数
		fetch.setRedirectNum(2);
		fetch.setAllowRedirect(true);
		//设置cookie
		fetch.setCookie("timestamp", System.currentTimeMillis()+ "");

		/***************3.发起请求****************/

		/****************发起一次get请求**********/
		fetch.get("http://iapp.wakao.me/iapk/offerList/1");
		System.out.println("----------Http Request(GET)----------");
		//获取http code
		int code= fetch.getHttpCode();
		System.out.println("--------------Http Code--------------");
		System.out.println(code);
		//获取返回的头部信息
		Map<String, String> header = fetch.getResponseHeader();
		System.out.println("--------------Http Header------------");
  		for(Map.Entry me : header.entrySet()){
  			System.out.println(me.getKey() + " : " + me.getValue());
 		}
		//获取返回的包体长度
		int len = fetch.getResponseBodyLen();
		System.out.println("--------------Http Length------------");
		System.out.println(len);

		//获取返回的包体数据
		System.out.println("--------------Http Body------------");
		String content = fetch.getResponseBody();
		System.out.println(content);
	}
}