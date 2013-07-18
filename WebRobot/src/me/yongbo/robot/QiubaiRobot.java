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

import me.yongbo.bean.QiubaiObj;
import me.yongbo.dbhelper.QiubaiDbHelper;
import me.yongbo.robot.util.HttpUtil;
import me.yongbo.robot.util.PropertieUtil;


public class QiubaiRobot extends WebRobot {
	
	private final static String HOST = "www.qiushibaike.com";
	private final static String REFERER = "www.qiushibaike.com";
	private final static String POINT_URL = "http://www.qiushibaike.com/%1$s/page/%2$d?s=4580238&slow";

	private int startPage;
	private int endPage;
	private String category;
	private QiubaiDbHelper dbHelper;
	
	private String lastTagId = null;
	
	private int failCount = 0; //失败次数记录
	private final static int MAX_FAILCOUNT = 3; //最多失败次数，请求某个URL失败超过这个次数将自动停止发起请求
	/**
	 * 构造函数
	 * @param stratPage 开始页码
	 * @param ctaegory 分类
	 * */
	public QiubaiRobot(int startPage, String category) {
		this(startPage, -1, category);
	}
	
	/**
	 * 构造函数
	 * @param stratPage 开始页码
	 * @param endPage 结束页码
	 * @param ctaegory 分类
	 * */
	public QiubaiRobot(int startPage, int endPage, String category) {
		this(startPage, endPage, category, null);
	}
	
	/**
	 * 构造函数,如果对该类目下是第二次抓取，使用该构造函数即可
	 * @param ctaegory 分类
	 * */
	public QiubaiRobot(String category) {
		this(1, -1, category, null);
	}

	public QiubaiRobot(int startPage, int endPage, String category, String lastTagId) {
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.category = category;
		this.lastTagId = lastTagId;
		this.startPage = startPage;
		this.endPage = endPage;
		this.dbHelper = new QiubaiDbHelper();
	}
	
	@Override
	public void run() {
		while (doAgain) {
			if(endPage != -1 && startPage > endPage){
				break;
			}
			doWork();
		}
	}
	public void doWork() {
		System.out.println("开始抓取第  " + startPage + " 页的数据");
		String rp;
		try {
			rp = getResponseString(String.format(POINT_URL, category, startPage));
			//System.out.println(rp);
			if(rp != null && rp.trim().length() > 0){ 
				parseHtml2Obj(rp);
				//dbHelper.execute("saveQBdata", parseHtml2Obj(rp));
				startPage++;
				failCount = 0;
			} else {
				failCount++;
				if(failCount > MAX_FAILCOUNT){
					doAgain = false;
					return;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			doAgain = false;
			e.printStackTrace();
		}
	}
	public List<QiubaiObj> parseHtml2Obj(String html){
		Document doc = Jsoup.parse(html);
		Elements eles = doc.getElementsByClass("block");
		if(eles.isEmpty()){
			doAgain = false;
			System.out.println("数据为空");
			return null;
		}
		QiubaiObj qbObj = null;
		List<QiubaiObj> qbObjs = new ArrayList<QiubaiObj>();
		for(Element ele : eles) {
			Element content = ele.getElementsByClass("content").first();
			Elements img = ele.select(".thumb img");
			Elements detail = ele.select(".detail a");
			
			qbObj = new QiubaiObj();
			qbObj.setId(getFilterId(ele.attr("id")));
			qbObj.setCreatetime(content.attr("title"));
			qbObj.setContent(content.text());
			qbObj.setDetailUrl(detail.get(0).attr("href"));
			if(!img.isEmpty()){
				qbObj.setImgUrl(img.get(0).attr("src"));
			}
			System.out.println(qbObj.getId());
			if(startPage == 1 && lastTagId == null) {
				lastTagId = qbObj.getId();
				PropertieUtil.write("lastTagId", lastTagId);
			} else if(lastTagId != null && lastTagId.equals(qbObj.getId())){
				doAgain = false;
				break;
			}
			qbObjs.add(qbObj);
		}
		return qbObjs;
	}
	/**
	 * 设置http请求的头信息
	 * */
	private static Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		return param;
	}
	
	private String getFilterId(String id){
		Pattern p = Pattern.compile("qiushi_tag_([0-9]+)");
        Matcher match = p.matcher(id);
        if (match.find()) {
        	return match.group(1);
        }
		return null;
	}
}
