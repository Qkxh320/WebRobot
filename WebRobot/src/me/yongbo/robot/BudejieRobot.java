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

public class BudejieRobot extends WebRobot2 {
	
	private final static String HOST = "www.budejie.com";
	private final static String REFERER = "http://www.budejie.com/";

	private final static String POINT_URL = "http://www.budejie.com/xcs.php";
	
	@Override
	public void run() {
		doWork();
		shutdownRobot();
	}

	public List<FunnyObj> doWork() {
		System.out.println("开始抓取百思不得姐数据");
		String rp = getResponseString(POINT_URL);
		List<FunnyObj> objs = new ArrayList<FunnyObj>();
		if (rp != null && rp.trim().length() > 0) {
			System.out.println(rp);
			objs = parseHtml2Obj(rp);
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

	@Override
	protected List<FunnyObj> parseHtml2Obj(String html) {
		Document doc = Jsoup.parse(html);
		Elements eles = doc.getElementsByClass("web_conter");
		
		if (eles.isEmpty()) {
			System.out.println("数据为空。。。");
			return null;
		}
		
		List<FunnyObj> objs = new ArrayList<FunnyObj>();
		FunnyObj obj = null;
		
		for (Element ele : eles) {
			Element content = ele.getElementsByClass("web_size").first();
			Elements author = ele.select(".web_time a");

			obj = new FunnyObj();
			obj.setId(getFilterId(content.attr("id")));
			obj.setCreateTime(dateFormat.format(new Date()));
			obj.setContent(content.text());
			obj.setFrom("百思不得姐");
			if(!author.isEmpty()){
				obj.setUserName(author.get(0).text().trim());
			}
			System.out.println(obj.getContent());
			dbRobot.addFunnyData(obj);
			objs.add(obj);
		}
		return null;
	}

	private String getFilterId(String id) {
		Pattern p = Pattern.compile("title-([0-9]+)");
		Matcher match = p.matcher(id);
		if (match.find()) {
			return match.group(1);
		}
		return null;
	}
}
