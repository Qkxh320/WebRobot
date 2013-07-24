package me.yongbo;


import me.yongbo.robot.HuabanRobot;
import me.yongbo.robot.Meitu91Robot;
import me.yongbo.robot.QiubaiRobot;
import me.yongbo.robot.util.PropertieUtil;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//http://meitu91.b0.upaiyun.com/
		new Thread(new QiubaiRobot(1, "late", PropertieUtil.read("lastTagId"), false)).start(); //抓取最新下所有数据
		//new Thread(new Meitu91Robot(1, 100，false)).start(); //抓取最新下所有数据
		//new Thread(new HuabanRobot("93115883", "pets")).start(); 
	}
}