package me.yongbo;


import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import me.yongbo.robot.DatabaseRobot;
import me.yongbo.robot.HuabanRobot;
import me.yongbo.robot.LengxiaohuaRobot;
import me.yongbo.robot.Meitu91Robot;
import me.yongbo.robot.QiubaiRobot;
import me.yongbo.robot.TouTiaoRobot;
import me.yongbo.robot.util.PropertieUtil;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		/**
		 * 使用示例
		 * */
		//new Thread(new QiubaiRobot(1, 1, "late")).start(); //抓取最新下所有数据
		
		/**
		 * 使用示例
		 * */
		//http://meitu91.b0.upaiyun.com/
		//new Thread(new Meitu91Robot(1, 100, true)).start(); //抓取最新下数据

		/**
		 * 使用示例
		 * */
		//new Thread(new HuabanRobot("94060078", "pets", false)).start(); //抓取宠物下数据
		
		/**
		 * 使用示例
		 * */
		//new Thread(new TouTiaoRobot(1, 1)).start(); //抓取段子下所有数据
		
		/**
		 * 使用示例
		 * */
		new Thread(new LengxiaohuaRobot(1, 1)).start(); //抓取段子下所有数据
		
	}
}