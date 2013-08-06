package me.yongbo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @author yongbo_
 * @time 2013/6/24
 * 
 * */
public class RobotClient implements Runnable {
	private String lang;
	private int book;
	private int endChapter;
	private int chapter;
	private String version;
	
	private boolean errorFlag = false;
	private final static String CONTENT = "创%1$d:%2$d %3$s";
	private final static String PointUrl = "http://www.yawill.com/viewbook.php?lang=%1$s&ver=%2$s&book=%3$d&chapter=%4$d";
	
	/**
	 * 构造函数
	 * @param lang 语言
	 * @param version 版本
	 * @param book 目录
	 * @param startChapter 开始章节
	 * @param endChapter 结束章节
	 * */
	public RobotClient(String lang,String version, int book, int startChapter,int endChapter) {
		this.lang = lang;
		this.book = book;
		this.chapter = startChapter;
		this.endChapter = endChapter;
		this.version = version;
	}

	public void doWork() {
		String url = String.format(PointUrl, lang, version, book, chapter);
		String responseStr;
		try {
			responseStr = httpGet(url);
			doFilter(responseStr.replaceAll("\\s+", ""));
			chapter++;
		} catch(Exception e){
			errorFlag = true;
			e.printStackTrace();
		}
	}

	/**
	 * 发起http请求
	 * @param webUrl 请求连接地址
	 * */
	public String httpGet(String webUrl) throws Exception {
		URL url;
		URLConnection conn;
		StringBuilder sb = new StringBuilder();
		url = new URL(webUrl);
		conn = url.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bufReader = new BufferedReader(isr);
		String lineText;
		while ((lineText = bufReader.readLine()) != null) {
			sb.append(lineText);
		}
		return sb.toString();
	}
	
	public void doFilter(String oldStr){
		Pattern p = Pattern.compile("<fontstyle='font-size=14px;'class='font'dir=\"ltr\">(.*?)</font>",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(oldStr);
		int i=1;
		while (m.find()) {
			if(!m.group(1).equals("<font>")){
				//System.out.println(m.group(0));
				System.out.println(String.format(CONTENT, chapter, i, m.group(1)));
				System.out.println("-----");
				i++;
			}
		}
	}
	@Override
	public void run() {
		while(!errorFlag && chapter <= endChapter){
			doWork();
		}
		System.out.println("ending...");
	}
	
}
