package me.yongbo;


import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import me.yongbo.robot.DatabaseRobot;
import me.yongbo.robot.HuabanRobot;
import me.yongbo.robot.LengxiaohuaRobot;
import me.yongbo.robot.Meitu91Robot;
import me.yongbo.robot.QiubaiRobot;
import me.yongbo.robot.TouTiaoRobot;
import me.yongbo.robot.WebRobot;
import me.yongbo.robot.WeixinArticleRobot;
import me.yongbo.robot.util.HttpUtil;
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
		//new Thread(new TouTiaoRobot(1, 10)).start(); //抓取段子下所有数据
		
		/**
		 * 使用示例
		 * */
		//new Thread(new LengxiaohuaRobot(1, 1)).start(); //抓取段子下所有数据
		
		
		//new Thread(new WeixinArticleRobot(0, "taobaoguijiaoqi", "鬼脚七")).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, "ayawawavip", "娃娃微信答")).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, "nba_story", "球人")).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, "wechanger", "改变自己")).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, "Upoetry", "诗歌精选")).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, "jixiaozhanV5", "老纪读书")).start(); //抓取段子下所有数据
		int i=0;
		WebRobot robot = new WebRobot(HttpUtil.getHttpGet(null));
		while(i<990){
			robot.getResponseString("http://www.teamtop.com/12jingling/index.php?m=index&a=vote&id=67&infloat=yes&handlekey=vote_vote&t=1380204754691&inajax=1&ajaxtarget=fwin_content_vote_vote");
			i++;
		}
	}
}