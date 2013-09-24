package me.yongbo.robot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import me.yongbo.robot.util.HttpUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class ImageRobot {
	protected final static int MAX_FAILCOUNT = 5; // 最多失败次数，请求某个URL失败超过这个次数将自动停止发起请求
	// 线程池
	private static ExecutorService pool;
	private static SimpleDateFormat sdf;
	
	//private HttpClient client;
	
	public ImageRobot(){
		
	}
	
	static {
		sdf = new SimpleDateFormat("/yyyyMMdd/HHmm/");
		pool = Executors.newFixedThreadPool(100); // 固定线程池
	}
	
	public static HttpGet getHttpGet(String url){
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", HttpUtil.USER_AGENT);
		if(Pattern.matches("^http://essay.*", url)){
			get.setHeader("Host", "essay.oss.aliyuncs.com");
			get.setHeader("Referer", "http://chuansongme.com");
		} else if(Pattern.matches("http://pic\\.qiushibaike\\.com", url)){
			get.setHeader("Host", "pic.qiushibaike.com");
		}
		return get;
	}
	
	public void downImage(String imgUrl, String root){
		Date date = new Date();
		String folderPath = root + sdf.format(date);
		String[] p = imgUrl.split("/");
		String fileName = p[p.length - 2] + "-" + p[p.length - 1];
		downImage(imgUrl, folderPath, fileName);
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
				int failCount = 0;
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
								+ "次下载失败,正在尝试重新下载..." + e.getMessage());
					} finally {
						
					}
				} while (failCount < MAX_FAILCOUNT);
			}
		});
	}
	
	public static byte[] readFromResponse(HttpEntity entity) throws Exception {
		InputStream inStream = entity.getContent();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//long length = entity.getContentLength();
		// 显示文件大小格式：2个小数点显示
		//DecimalFormat df = new DecimalFormat("0.00");
		// 总文件大小
		//String fileSize = df.format((float) length / 1024 / 1024) + "MB";
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
}
