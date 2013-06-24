package me.yongbo;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * 使用示例
		 * */
		//Thread thread0 = new Thread(new RobotClient("zh", "cuv", 1, 1, 50));
		//thread0.start();
		
		Thread thread1 = new Thread(new RobotClient("zh", "cuv", 2, 1, 50));
		thread1.start();
	}

}
