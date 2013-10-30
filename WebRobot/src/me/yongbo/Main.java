package me.yongbo;


import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import me.yongbo.robot.BudejieRobot;
import me.yongbo.robot.DataRobot;
import me.yongbo.robot.DatabaseRobot;
import me.yongbo.robot.HuabanRobot;
import me.yongbo.robot.LengxiaohuaRobot;
import me.yongbo.robot.Meitu91Robot;
import me.yongbo.robot.QiubaiRobot;
import me.yongbo.robot.TouTiaoRobot;
import me.yongbo.robot.WebRobot;
import me.yongbo.robot.WebRobot2;
import me.yongbo.robot.WeixinArticleRobot;
import me.yongbo.robot.bean.ChannelObj;
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
		//new Thread(new QiubaiRobot(1, 1, "hot")).start(); //抓取最新下所有数据
		/**
		 * 使用示例
		 * */
		//new Thread(new TouTiaoRobot(1, 1,"joke")).start(); //抓取段子下所有数据
		//new Thread(new TouTiaoRobot(1, 1,"weirdo")).start(); //抓取段子下所有数据
		
		/**
		 * 使用示例
		 * */
		//new Thread(new LengxiaohuaRobot(1, 1)).start(); //抓取段子下所有数据
		
		//new Thread(new BudejieRobot()).start(); //抓取段子下所有数据
		/**
		 * 使用示例
		 * */
		//http://meitu91.b0.upaiyun.com/
		//new Thread(new Meitu91Robot(1, 100, true)).start(); //抓取最新下数据

		/**
		 * 使用示例
		 * */
		//new Thread(new HuabanRobot("94060078", "pets", false)).start(); //抓取宠物下数据
		
		
		
		//new Thread(new WeixinArticleRobot(0, "taobaoguijiaoqi", "鬼脚七", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		new Thread(new WeixinArticleRobot(0, 12, "his-tory", "我们爱历史", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		new Thread(new WeixinArticleRobot(0, 12, "gobybike", "骑行西藏", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, 12, "duokan", "多看阅读", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, "nanfangzhoumo", "南方周末", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0,12, "nba_story", "球人", ChannelObj.CHANNEL_NBA)).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0,12, "wechanger", "改变自己", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0,12, "igandeshuang", "听甘德霜讲故事", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, 12, "luojisw", "罗辑思维", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
		//new Thread(new WeixinArticleRobot(0, "Upoetry", "诗歌精选", ChannelObj.CHANNEL_RECOMMEND)).start(); //抓取段子下所有数据
//		int i=0;
//		WebRobot robot = new WebRobot(HttpUtil.getHttpGet(null));
//		while(i<100){
//			robot.getResponseString("http://www.teamtop.com/12jingling/index.php?m=index&a=vote&id=67&infloat=yes&handlekey=vote_vote&t=1380204754691&inajax=1&ajaxtarget=fwin_content_vote_vote");
//			i++;
//		}
//		new WebRobot2(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			protected Map<String, String> getRequestHeaders() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			protected Object parseHtml2Obj(String html) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//		}.writeToCache("E:/weixin_article/cache/", "nba_story", "http://chuansongme.com/n/188575");
	}
}
