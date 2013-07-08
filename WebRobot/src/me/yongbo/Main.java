package me.yongbo;

import me.yongbo.robot.Meitu91Robot;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//http://meitu91.b0.upaiyun.com/
		//Thread thread01 = new Thread(new Meitu91Robot(2251));
		//thread01.start();
		for(int k=0;k<100;k++){
			Thread thread02 = new Thread(new Runnable() {
				public void run() {
					int i = 0;
					Meitu91Robot r = new Meitu91Robot(1);
					while(true){
						if(i++ > 1000){break;}
						try {
							r.getResponseString("http://www.91meitu.net/img-item/user-preference?type=like&id=7459");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			thread02.start();
		}
		
		
	}
}