package me.yongbo;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.yongbo.robot.Meitu91Robot;
import me.yongbo.robot.QiubaiRobot;
import me.yongbo.robot.WebRobot;
import me.yongbo.robot.util.PropertieUtil;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//http://meitu91.b0.upaiyun.com/
		//new Thread(new QiubaiRobot(1, "late")).start(); //抓取最新下所有数据
		//new Thread(new Meitu91Robot(1)).start(); //抓取最新下所有数据
		//PropertieUtil.write("tagId", "5556");
		//System.out.println(PropertieUtil.read("tagId"));
		String imgUrl = "http://img4.cache.netease.com/photo/0001/2013-07-17/940O35BC3R710001.jpg";
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String fileType = imgUrl.substring(imgUrl.lastIndexOf(".") - 1);
		String fileName = format.format(date) + fileType;
		new WebRobot().downImage(imgUrl, WebRobot.default_img_savedir, fileName);
	}
}