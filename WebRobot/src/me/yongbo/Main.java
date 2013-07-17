package me.yongbo;

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
		Thread thread01 = new Thread(new QiubaiRobot(109, "late")); //抓取最新下所有数据
		thread01.start();
//		Thread thread01 = new Thread(new Meitu91Robot(1)); //抓取最新下所有数据
//		thread01.start();
		//PropertieUtil.write("tagId", "5556");
		//System.out.println(PropertieUtil.read("tagId"));
	}
}