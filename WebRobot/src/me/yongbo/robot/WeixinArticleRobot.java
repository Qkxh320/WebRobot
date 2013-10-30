package me.yongbo.robot;

import java.util.Date;
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
	
	private final String ROOT = "E:/img_article/";
	private final static String CACHE_DIR = "E:/weixin_article/cache/"; //序列化数据缓存目录
	
	private int lastIndex;
	private String account;
	private String account_desc;
	private int channel;
	private int max;
	private int cur_count = 0;
	
	private String cacheUrl = null;
	
	
	public WeixinArticleRobot(int lastIndex, String account, int channel) {
		this(lastIndex, -1, account, account, channel);
	}
	public WeixinArticleRobot(int lastIndex, String account, String account_desc, int channel) {
		this(lastIndex, -1, account, account_desc, channel);
	}
	public WeixinArticleRobot(int lastIndex, int max, String account, String account_desc, int channel) {
		this.lastIndex = lastIndex;
		this.max = max;
		this.account = account;
		this.account_desc = account_desc;
		this.channel = channel;
	}
	public void initCacheUrl(){
		this.cacheUrl = (String)readFromCache(CACHE_DIR, account);
	}
	public void setCacheUrl(String cacheValue){
		writeToCache(CACHE_DIR, account, cacheValue);
	}
	
	@Override
	public void run() {
		initCacheUrl();
		while (doAgain) {
			if(max != -1 && cur_count >= max){ break; }
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
			String href = ele.attr("href");
			
			if(cur_count == 0){
				setCacheUrl(href);
			}
			if(cacheUrl != null && cacheUrl.equals(href)) {
				doAgain = false;
				System.err.println("新数据抓取完毕..." + account + "抓取线程正在结束...");
				return;
			}

			System.out.println(ele.text());
			String html = getResponseString(ele.attr("href"));
			parseHtml2Obj(html);
		}
	}

	public String getMySavePath(String imgUrl){
		Date date = new Date();
		String folderPath = sdf.format(date);
		String[] p = imgUrl.split("/");
		String fileName = p[p.length - 2] + "-" + p[p.length - 1];
		return folderPath + fileName;
	}

	@Override
	public Object parseHtml2Obj(String html) {
		Document doc = Jsoup.parse(html);
		Element title = doc.getElementById("activity-name");
		Element createtime = doc.getElementById("post-date");
		Element content = doc.getElementById("essay-body");
		Elements imgs = doc.select(".text img");
		if(!imgs.isEmpty()){
			for (Element img : imgs) {
				String pic_url = getSrc(img.attr("src"));
				String mySavePath = getMySavePath(img.attr("src"));
				img.attr("src", mySavePath);
				//System.out.println(ROOT + mySavePath);
				imgRobot.downImage(pic_url, ROOT + mySavePath);
			}
		}
		
		Elements pic = doc.select("#media img");
		Elements _intro = doc.select(".text p");
		String intro = null;
		if(_intro.isEmpty()){
			intro = "阅读全部";
		} else {
			intro = _intro.first().text();
		}
		
		ArticleObj obj = new ArticleObj();
		if(!pic.isEmpty()){
			String src = pic.get(0).attr("src");
			String pic_url = getSrc(src);
			String mySavePath = getMySavePath(pic_url);
			pic.attr("src", mySavePath);
			obj.setPic(mySavePath);
			System.err.println(obj.getPic());
			imgRobot.downImage(pic_url, ROOT + mySavePath);
		}
		
		obj.setAuthor(account);
		obj.setFrom(account_desc);
		obj.setContent(content.html());
		obj.setCreatetime(createtime.text());
		obj.setTitle(title.text());
		obj.setChannel(channel);
		obj.setIntro(intro.substring(0, intro.length() > 50 ? 50 : intro.length()) + "...");
		dbRobot.addArticleData(obj);
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
