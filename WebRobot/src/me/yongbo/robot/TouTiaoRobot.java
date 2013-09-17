package me.yongbo.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import me.yongbo.robot.bean.FunnyObj;
import me.yongbo.robot.bean.TouTiaoObj;
import me.yongbo.robot.bean.TouTiaoResponse;

public class TouTiaoRobot extends WebRobot2 {
	private final static String HOST = "www.toutiao.com";
	private final static String REFERER = "www.toutiao.com";
	private final static String POINT_URL = "http://www.toutiao.com/api/essay/recent/recent?callback=&tag=joke&count=20&max_behot_time=1379379925&offset=0&_=1379387432983";
	
	
	
	private int startPage;
	private int endPage;
	public TouTiaoRobot(int startPage) {
		this.startPage = startPage;
		this.endPage = -1;
	}
	/**
	 * 构造函数
	 * 
	 * @param stratPage
	 *            开始页码
	 * @param endPage
	 *            结束页码
	 * @param ctaegory
	 *            分类
	 * @param databaseEnable
	 *            是否写入数据库
	 * */
	public TouTiaoRobot(int startPage, int endPage) {
		this.startPage = startPage;
		this.endPage = endPage;
	}
	
	@Override
	public void run() {
		while (doAgain) {
			if (endPage != -1 && startPage > endPage) { break; }
			doWork();
		}
		shutdownRobot();
	}
	
	public List<FunnyObj> doWork() {
		System.out.println("开始抓取第  " + startPage + " 页的数据");
		String rp = getResponseString(String.format(POINT_URL, startPage));
		
		List<FunnyObj> objs = new ArrayList<FunnyObj>();

		if (rp != null && rp.trim().length() > 0) {
			System.out.println(rp);
			objs = parseHtml2Obj(rp);
			startPage++;
		}
		return objs;
	}

	public List<FunnyObj> parseHtml2Obj(String json) {
		List<FunnyObj> objs = new ArrayList<FunnyObj>();
		TouTiaoResponse res = (TouTiaoResponse)gson.fromJson(json, TouTiaoResponse.class);

		for (FunnyObj obj : res.getData()) {
			TouTiaoObj tObj = (TouTiaoObj)obj;
			obj.setFrom("头条网");
			obj.setCreateTime(tObj.getDatetime());
			obj.setContent(tObj.getText());
			objs.add(obj);
		}
		return objs;
	}

	@Override
	protected Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		return param;
	}
}
