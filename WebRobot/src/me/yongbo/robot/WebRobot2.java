package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import me.yongbo.robot.util.HttpUtil;


import com.google.gson.Gson;

public abstract class WebRobot2 implements Runnable {
	protected final static String TCP = "http://";
	
	protected Gson gson;
	protected HttpClient httpClient;
	protected HttpGet getMethod;

	protected static SimpleDateFormat sdf;
	protected static SimpleDateFormat dateFormat;
	
	protected boolean isfirst = true;

	// 标志位，用于指示线程是否循环执行
	protected boolean doAgain = true;
	// 线程池
	private static ExecutorService pool;

	// protected int failCount = 0; //抓取失败次数记录
	protected final static int MAX_FAILCOUNT = 5; // 最多失败次数，请求某个URL失败超过这个次数将自动停止发起请求

	
	//protected static RobotCache cache;
	
	protected DataRobot dbRobot;
	
	protected ImageRobot imgRobot;
	
	static {
		pool = Executors.newFixedThreadPool(50); // 固定线程池
		sdf = new SimpleDateFormat("yyyyMMdd/HHmm/");
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//cache = new RobotCache();
	}

	/**
	 * 构造函数
	 * */
	public WebRobot2() {
		this.httpClient = HttpUtil.getHttpClient();
		this.getMethod = HttpUtil.getHttpGet(getRequestHeaders());
		this.gson = new Gson();
		this.dbRobot = new DataRobot();
		this.imgRobot = new ImageRobot();
	}
	/**
	 * 通过URL地址获取页面的html字符串
	 * 
	 * @param url
	 *            目标地址
	 * */
	public String getResponseString(String url) {
		String lineText;
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		int failCount = 0;
		do {
			try {
				getMethod.setURI(new URI(url));
				HttpResponse response = httpClient.execute(getMethod);
				int status = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				if (status != HttpStatus.SC_OK || entity == null) {
					return null;
				}
				isr = new InputStreamReader(entity.getContent());
				BufferedReader bufReader = new BufferedReader(isr);
				while ((lineText = bufReader.readLine()) != null) {
					sb.append(lineText);
				}
				break;
			} catch (Exception e) {
				failCount++;
				System.err.println("对于链接:" + url + " 第" + failCount
						+ "次抓取失敗，正在尝试重新抓取...");
			} finally {
				try {
					if (isr != null) {
						isr.close();
					}
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		} while (failCount < MAX_FAILCOUNT);
		return sb.toString();
	}
	
	public static HttpGet getHttpGet(String url){
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", HttpUtil.USER_AGENT);
		if(Pattern.matches("^http://essay.*", url)){
			get.setHeader("Host", "essay.oss.aliyuncs.com");
			get.setHeader("Referer", "http://chuansongme.com");
		}else if(Pattern.matches("http://pic\\.qiushibaike\\.com", url)){
			get.setHeader("Host", "pic.qiushibaike.com");
		}
		return get;
	}

	/**
	 * 下载网络图片到本地
	 * 
	 * @param imgUrl
	 *            网络图片路径
	 * @param folderPath
	 *            本地存放目录
	 * @param fileName
	 *            存放的文件名
	 * */
	public void downImage(final String imgUrl, final String folderPath,
			final String fileName) {
		System.out.println("开始下载图片:" + imgUrl);
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		pool.execute(new Runnable() {
			@Override
			public void run() {
				HttpClient client = HttpUtil.getHttpClient();
				HttpGet get = getHttpGet(imgUrl);
				int failCount = 1;
				do {
					try {
						HttpResponse response = client.execute(get);
						HttpEntity entity = response.getEntity();
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && entity != null) {
							byte[] data = readFromResponse(entity);
							String savePaht = folderPath + fileName;
							File imageFile = new File(savePaht);
							FileOutputStream outStream = new FileOutputStream(
									imageFile);
							outStream.write(data);
							outStream.close();
						}
						break;
					} catch (Exception e) {
						failCount++;
						System.err.println("对于图片" + imgUrl + "第" + failCount
								+ "次下载失败,正在尝试重新下载...");
					} finally {
						
					}
				} while (failCount < MAX_FAILCOUNT);
			}
		});
	}

	public static byte[] readFromResponse(HttpEntity entity) throws Exception {
		InputStream inStream = entity.getContent();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		long length = entity.getContentLength();
		// 显示文件大小格式：2个小数点显示
		DecimalFormat df = new DecimalFormat("0.00");
		// 总文件大小
		String fileSize = df.format((float) length / 1024 / 1024) + "MB";
		//缓存
		byte[] buffer = new byte[1024];
		
		int len = 0;
		// int count = 0;
		// String processText;
		long t = System.currentTimeMillis();

		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
			// 下载进度
			// count += len;
			// processText = df.format((float) count / 1024 / 1024) + "MB" + "/"
			// + fileSize;
			// System.out.println(processText);
		}
		System.out
				.println("下载完成，耗时：" + (System.currentTimeMillis() - t) + "毫秒");
		inStream.close();
		return outStream.toByteArray();
	}
	
	/**
	 * 释放httpclient资源
	 * */
	protected void shutdownRobot(){
		httpClient.getConnectionManager().shutdown();
	}
	/**
	 * 写入缓存
	 * */
	public void writeToCache(String cacheDir, String cacheKey, String cacheValue){
		int cnt = 0;
		do {
			try {
				File destDir = new File(cacheDir);
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
	            ObjectOutputStream os = new ObjectOutputStream(  
	                    new FileOutputStream(cacheDir + cacheKey));  
	            os.writeObject(cacheValue);  
	            os.close();
	            break;
	        } catch (Exception e) { 
	        	cnt++;
	            System.err.println("写入缓存失败,正在尝试第" + cnt + "次重新写入");
	        } 
		} while(cnt < MAX_FAILCOUNT);
		
	}
	/**
	 * 读取缓存
	 * */
	public Object readFromCache(String cacheDir, String cacheKey){
		File file = new File(cacheDir + cacheKey);
		if(!file.exists()){
			return null;
		}
		
		Object cacheObj = null;
		int cnt = 0;
		do {
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));  
				cacheObj = is.readObject();
	            is.close();
	            break;
	        } catch (Exception e) { 
	        	e.printStackTrace();
	        	cnt++;
	            System.err.println("读取缓存失败,正在尝试第" + cnt + "次重新读取");
	        } 
		} while(cnt < MAX_FAILCOUNT);
		return cacheObj;
	}
	
	protected abstract Map<String, String> getRequestHeaders();
	protected abstract Object parseHtml2Obj(String html);
}
