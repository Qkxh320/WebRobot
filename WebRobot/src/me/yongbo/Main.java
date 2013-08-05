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
		
		/**
		 * 使用示例
		 * */
		//new Thread(new QiubaiRobot(1, "late", false)).start(); //抓取最新下所有数据
		
		
		/**
		 * 使用示例
		 * */
		//http://meitu91.b0.upaiyun.com/
		//new Thread(new Meitu91Robot(1, 100, true)).start(); //抓取最新下数据
		
		
		/**
		 * 使用示例
		 * */
		new Thread(new HuabanRobot("94060078", "pets", false)).start(); //抓取美女下数据
	}
}