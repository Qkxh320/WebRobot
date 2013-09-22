package me.yongbo.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.yongbo.robot.bean.ArticleObj;

public class WeixinArticleRobot extends WebRobot2 {

	private final static String HOST = "chuansong.me";
	private final static String REFERER = "chuansong.me";
	private final static String POINT_URL = "http://chuansong.me/more/account-%1$s/recent?lastindex=%2$d";
	
	private int lastIndex;
	private String account;
	private String account_desc;
	private int max;
	private int cur_count = 0;
	
	public WeixinArticleRobot(int lastIndex, String account) {
		this(lastIndex, -1, account, account);
	}
	public WeixinArticleRobot(int lastIndex, String account, String account_desc) {
		this(lastIndex, -1, account, account_desc);
	}
	public WeixinArticleRobot(int lastIndex, int max, String account, String account_desc) {
		this.lastIndex = lastIndex;
		this.max = max;
		this.account = account;
		this.account_desc = account_desc;
	}
	
	@Override
	public void run() {
		while (doAgain) {
			if(max != -1 && cur_count > max){ break; }
			doWork();
		}
		shutdownRobot();
	}
	
	public void doWork() {
		String rp = getResponseString(String.format(POINT_URL, account,
				lastIndex));
		System.out.println(rp);
		
		if (rp != null && rp.trim().length() > 0) {
			getArticle(rp);
		}
		lastIndex += 12;
	}
	
	private void getArticle(String rp) {
		Document doc = Jsoup.parse(rp);
		Elements eles = doc.getElementsByClass("question_link");
		String hasNext = doc.getElementById("hn"+(lastIndex+12)).text();
		if (hasNext.equals("0")) {
			doAgain = false;
		}
		for (Element ele : eles) {
			System.out.println(ele.text());
			String html = getResponseString(ele.attr("href"));
			parseHtml2Obj(html);
		}
		
	}

	@Override
	public Object parseHtml2Obj(String html) {
		Document doc = Jsoup.parse(html);
		Element title = doc.getElementById("activity-name");
		Element createtime = doc.getElementById("post-date");
		//Element from = doc.getElementById("post-user");
		Element content = doc.getElementById("essay-body");
		Elements pic = doc.select("#media img");
		Elements _intro = doc.select(".text p");
		String intro = null;
		if(_intro.isEmpty()){
			intro = "阅读全部";
		}else{
			intro = _intro.first().text();
		}
		
		//List<ArticleObj> objs = new ArrayList<ArticleObj>();
		ArticleObj obj = new ArticleObj();
		obj.setAuthor(account);
		obj.setFrom(account_desc);
		obj.setContent(content.html());
		obj.setCreatetime(createtime.text());
		obj.setTitle(title.text());
		obj.setIntro(intro.substring(0, intro.length() > 50 ? 50 : intro.length()) + "...");
		if(!pic.isEmpty()){
			String src = pic.get(0).attr("src");
			obj.setPic(getSrc(src));
		}
		System.err.println(obj.getPic());
		dbRobot.AddArticleData(obj);
		cur_count++;
		return null;
	}
	
	@Override
	protected Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		return param;
	}
	private String getSrc(String src){
		if(Pattern.matches("^http.*", src)){
			return src;
		}
		
		return TCP + HOST + src;
	}
}
