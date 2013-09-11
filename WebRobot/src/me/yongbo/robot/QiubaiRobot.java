package me.yongbo.robot;

import java.util.ArrayList;
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
import me.yongbo.robot.dbhelper.FunnyDbHelper;
import me.yongbo.robot.util.HttpUtil;

public class QiubaiRobot extends WebRobot {

	public static String rootDir = "D:/wakao/webimage/qbimage/";
	private final static String BEFORE = "qb_";

	private final static String HOST = "www.qiushibaike.com";
	private final static String REFERER = "www.qiushibaike.com";
	private final static String POINT_URL = "http://www.qiushibaike.com/%1$s/page/%2$d?s=4580238&slow";

	private int startPage;
	private int endPage;
	private String category;
	private FunnyDbHelper dbHelper;

	private boolean isFirst;
	/**
	 * 构造函数
	 * 
	 * @param stratPage
	 *            开始页码
	 * @param ctaegory
	 *            分类
	 * */
	public QiubaiRobot(int startPage, String category) {
		this(startPage, -1, category, false);
	}
	public QiubaiRobot(int startPage, int endPage, String category) {
		this(startPage, endPage, category, false);
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

	public QiubaiRobot(int startPage, int endPage, String category, Boolean databaseEnable) {
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.category = category;
		this.startPage = startPage;
		this.endPage = endPage;
		this.databaseEnable = databaseEnable;
		this.isFirst = startPage == 1 ? true : false;
		this.dbHelper = new FunnyDbHelper();
	}

	@Override
	public void run() {
		while (doAgain) {
			if (endPage != -1 && startPage > endPage) {
				break;
			}
			doWork();
		}
		shutdownRobot();
	}

	private void handlerData(List<QiubaiObj> qbs) {
		List<FunnyObj> funnyObjs = new ArrayList<FunnyObj>();
		funnyObjs.addAll(qbs);
		// 写入数据库
		if (databaseEnable) {
			dbHelper.execute("saveFunnyData", funnyObjs);
		}
	}

	public void setStartPage(int page){
		this.startPage = page;
	}
	
	public List<QiubaiObj> doWork() {
		System.out.println("开始抓取第  " + startPage + " 页的数据");
		String rp = getResponseString(String.format(POINT_URL, category,
				startPage));
		
		List<QiubaiObj> qbs = new ArrayList<QiubaiObj>();
		if (rp != null && rp.trim().length() > 0) {
			
			qbs = parseHtml2Obj(rp);
			
			handlerData(qbs);
			
			startPage++;
		}
		return qbs;
	}

	public List<QiubaiObj> parseHtml2Obj(String html) {
		Document doc = Jsoup.parse(html);
		Elements eles = doc.getElementsByClass("block");
		List<QiubaiObj> qbObjs = new ArrayList<QiubaiObj>();
		if (eles.isEmpty()) {
			//doAgain = false;
			System.out.println("数据为空, 结束抓取。。。");
			return qbObjs;
		}
		QiubaiObj qbObj = null;
		for (Element ele : eles) {
			Element content = ele.getElementsByClass("content").first();
			Elements img = ele.select(".thumb img");
			Elements detail = ele.select(".detail a");

			qbObj = new QiubaiObj();
			qbObj.setId(getFilterId(ele.attr("id")));
			qbObj.setCreateTime(content.attr("title"));
			qbObj.setContent(content.text());
			qbObj.setSource(HOST + detail.get(0).attr("href"));
			qbObj.setFrom("糗事百科");
			if (!img.isEmpty()) {
				qbObj.setImgUrl(img.get(0).attr("src"));
			}
			qbObjs.add(qbObj);
		}
		return qbObjs;
	}

	/**
	 * 设置http请求的头信息
	 * */
	private static Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		return param;
	}

	private String getFilterId(String id) {
		Pattern p = Pattern.compile("qiushi_tag_([0-9]+)");
		Matcher match = p.matcher(id);
		if (match.find()) {
			return match.group(1);
		}
		return null;
	}
}
