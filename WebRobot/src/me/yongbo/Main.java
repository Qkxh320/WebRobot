package me.yongbo;

import me.yongbo.robot.Meitu91Robot;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Thread thread01 = new Thread(new Meitu91Robot(5001, 6000));
		thread01.start();
		//Thread thread02 = new Thread(new Meitu91Robot(40,60));
		//thread02.start();
	}
}