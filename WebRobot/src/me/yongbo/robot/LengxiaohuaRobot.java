package me.yongbo.robot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.yongbo.robot.bean.FunnyObj;
import me.yongbo.robot.bean.QiubaiObj;
import me.yongbo.robot.bean.TouTiaoObj;
import me.yongbo.robot.bean.TouTiaoResponse;
import me.yongbo.robot.dbhelper.FunnyDbHelper;
import me.yongbo.robot.util.HttpUtil;

public class LengxiaohuaRobot extends WebRobot2 {

	private final static String HOST = "lengxiaohua.com";
	private final static String REFERER = "www.lengxiaohua.com";
	private final static String POINT_URL = "http://lengxiaohua.com/new?page_num=%1$d";

	private int startPage;
	private int endPage;
	
	/**
	 * 构造函数
	 * 
	 * @param stratPage
	 *            开始页码
	 * */
	public LengxiaohuaRobot(int startPage) {
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
	 * */
	public LengxiaohuaRobot(int startPage, int endPage) {
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
		}
		startPage++;
		return objs;
	}

	public List<FunnyObj> parseHtml2Obj(String html) {
		Document doc = Jsoup.parse(html);
		Elements eles = doc.getElementsByClass("joke_li");
		
		if (eles.isEmpty()) {
			System.out.println("数据为空。。。");
			return null;
		}
		
		List<FunnyObj> objs = new ArrayList<FunnyObj>();
		FunnyObj obj = null;
		
		for (Element ele : eles) {
			Element content = ele.getElementsByClass("para_can").first();
			Elements img = ele.select(".default_load_imgbox img");
			Elements author = ele.select(".user_info a");

			obj = new FunnyObj();
			obj.setId(getFilterId(ele.attr("id")));
			obj.setCreateTime(dateFormat.format(new Date()));
			obj.setContent(content.text());
			obj.setFrom("我们爱讲冷笑话");
			if(!author.isEmpty()){
				obj.setUserName(author.get(0).text());
			}
			if (!img.isEmpty()) {
				obj.setImgUrl(TCP + HOST + img.get(0).attr("data-original"));
			}
			dbRobot.addFunnyData(obj);
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
	
	private String getFilterId(String id) {
		Pattern p = Pattern.compile("joke_li_([0-9]+)");
		Matcher match = p.matcher(id);
		if (match.find()) {
			return match.group(1);
		}
		return null;
	}
	@Override
	protected boolean chacheEndByCache(String cacheValue) {
		
		return false;
	}
}
