package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import me.yongbo.robot.bean.RobotCache;
import me.yongbo.robot.util.HttpUtil;


import com.google.gson.Gson;

public class WebRobot implements Runnable {
	protected Gson gson;
	protected HttpClient httpClient;
	protected HttpGet getMethod;

	protected static SimpleDateFormat sdf;

	// 标志位，用于指示线程是否循环执行
	protected boolean doAgain = true;
	// 是否写入数据库
	protected boolean databaseEnable = false;
	// 线程池
	private static ExecutorService pool;

	// protected int failCount = 0; //抓取失败次数记录
	protected final static int MAX_FAILCOUNT = 5; // 最多失败次数，请求某个URL失败超过这个次数将自动停止发起请求

	protected Boolean _DEBUG = true;
	
	protected static RobotCache cache;
	
	protected DatabaseRobot dbRobot;
	
	static {
		pool = Executors.newFixedThreadPool(20); // 固定线程池
		sdf = new SimpleDateFormat("yyyyMMdd/HHmm/");
		cache = new RobotCache();
	}

	/**
	 * 构造函数
	 * 
	 * @param getMethod
	 *            一个GetMethod实例
	 * */
	public WebRobot(HttpGet getMethod) {
		this.httpClient = HttpUtil.getHttpClient();
		this.getMethod = getMethod;
		gson = new Gson();
	}

	/**
	 * 构造函数
	 * */
	public WebRobot() {}

	public void setDatabaseEnable(boolean isDatabaseEnable){
		this.databaseEnable = isDatabaseEnable;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	public void setDebugMode(Boolean debug){
		this._DEBUG = debug;
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
				if(_DEBUG){
					System.err.println("对于链接:" + url + " 第" + failCount
							+ "次抓取失敗，正在尝试重新抓取...");
				}
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
				HttpGet get = new HttpGet(imgUrl);
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
						if(_DEBUG){
							System.err.println("对于图片" + imgUrl + "第" + failCount
									+ "次下载失败,正在尝试重新下载...");
						}
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
	
	protected void shutdownRobot(){
		httpClient.getConnectionManager().shutdown();
	}
	
	/*
	protected String curDir; //按照当前时间生成的目录
	protected String folderPath; //图片存放的目錄（绝对路径）

	public void initSaveDir(String rootDir) {
		Date date = new Date();
		curDir = sdf.format(date);
		folderPath = rootDir + curDir;
	}*/
}
