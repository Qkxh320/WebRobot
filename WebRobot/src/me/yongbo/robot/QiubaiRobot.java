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
	
	public static String rootDir = "D:/wakao/webimage/qbimage/";
	private final static String BEFORE = "qb_";
	
	private final static String HOST = "www.qiushibaike.com";
	private final static String REFERER = "www.qiushibaike.com";
	private final static String POINT_URL = "http://www.qiushibaike.com/%1$s/page/%2$d?s=4580238&slow";

	private int startPage;
	private int endPage;
	private String category;
	private QiubaiDbHelper dbHelper;
	
	private String lastTagId = null;
	
	
	
	private boolean isFirst;
	/**
	 * 构造函数
	 * @param stratPage 开始页码
	 * @param ctaegory 分类
	 * */
	public QiubaiRobot(int startPage, String category, String lastTagId, Boolean databaseEnable) {
		this(startPage, -1, category, lastTagId, databaseEnable);
	}
	
	/**
	 * 构造函数
	 * @param stratPage 开始页码
	 * @param endPage 结束页码
	 * @param ctaegory 分类
	 * @param lastTagId 最新数据标识符
	 * */

	public QiubaiRobot(int startPage, int endPage, String category, String lastTagId, Boolean databaseEnable) {
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.category = category;
		this.lastTagId = lastTagId;
		this.startPage = startPage;
		this.endPage = endPage;
		this.databaseEnable = databaseEnable;
		this.isFirst = startPage == 1 ? true : false;
		this.dbHelper = new QiubaiDbHelper();
	}
	
	@Override
	public void run() {
		while (doAgain && failCount <= MAX_FAILCOUNT) {
			if(endPage != -1 && startPage > endPage){
				break;
			}
			doWork();
		}
	}
	
	private void handlerData(List<QiubaiObj> qbs){
		/*
		Date date = new Date();
		String curDir = sdf.format(date);
		String folderPath = rootDir + curDir;*/
		initSaveDir(rootDir);
		for (QiubaiObj qb : qbs) {
			String imgUrl = qb.getImgUrl();
			if(imgUrl != null){
				String fileType = imgUrl.substring(imgUrl.lastIndexOf(".") - 1);
				String fileName = BEFORE + qb.getId() + fileType;
				qb.setSavePath(curDir + fileName);
				downImage(imgUrl, folderPath, fileName);
			}
		}
		//写入数据库
		if(databaseEnable){
			dbHelper.execute("saveQBdata", qbs);
		}
		
	}
	public void doWork() {
		System.out.println("开始抓取第  " + startPage + " 页的数据");
		String rp;
		try {
			rp = getResponseString(String.format(POINT_URL, category, startPage));
			if(rp != null && rp.trim().length() > 0) { 
				handlerData(parseHtml2Obj(rp));
				startPage++;
				failCount = 0;
			} else {
				failCount++;
			}
		} catch (Exception e) {
			failCount++;
			//e.printStackTrace();
			System.err.println("对于链接:" +String.format(POINT_URL, category, startPage) + " 第" + failCount+"次抓取失敗，正在尝试重新抓取...");
		}
	}
	public List<QiubaiObj> parseHtml2Obj(String html){
		Document doc = Jsoup.parse(html);
		Elements eles = doc.getElementsByClass("block");
		List<QiubaiObj> qbObjs = new ArrayList<QiubaiObj>();
		if(eles.isEmpty()){
			doAgain = false;
			System.out.println("数据为空, 结束抓取。。。");
			return qbObjs;
		}
		QiubaiObj qbObj = null;
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
			
			if(startPage == 1 && isFirst) {
				PropertieUtil.write("lastTagId", qbObj.getId());
				isFirst = false;
			}
			if(lastTagId != null && lastTagId.equals(qbObj.getId())){
				doAgain = false;
				System.out.println("新数据抓取完毕。。。");
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
