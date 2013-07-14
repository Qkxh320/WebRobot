package me.yongbo;

import me.yongbo.robot.QiubaiRobot;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//http://meitu91.b0.upaiyun.com/
		//new QiubaiRobot(1, "8hr").doWork();
		Thread thread01 = new Thread(new QiubaiRobot(1, 2, "8hr"));
		thread01.start();
	}
}