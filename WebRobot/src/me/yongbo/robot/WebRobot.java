package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.yongbo.robot.util.HttpUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

public class WebRobot implements Runnable {
	protected HttpClient httpClient;
	protected GetMethod getMethod;
	
	//标志位，用于指示线程是否继续执行（如遇到错误，则停止运行）
	protected boolean doAgain = true;
	
	private static ExecutorService pool;
	static {
		pool = Executors.newFixedThreadPool(20);  //固定线程池
	}
	/**
	 * 构造函数
	 * @param getMethod 一个GetMethod实例
	 * */
	public WebRobot(GetMethod getMethod) {
		this.httpClient = HttpUtil.getHttpClient();
		this.getMethod = getMethod;
		
	}
	/**
	 * 构造函数
	 * */
	public WebRobot() {}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	/**
	 * 通过URL地址获取页面的html字符串
	 * @param url 目标地址
	 * */
	public String getResponseString(String url) throws Exception {
		getMethod.setURI(new URI(url));
		int status = httpClient.executeMethod(getMethod);
		if(status != HttpStatus.SC_OK){
			return null;
		}
		InputStreamReader isr = new InputStreamReader(getMethod.getResponseBodyAsStream());
		BufferedReader bufReader = new BufferedReader(isr);
		String lineText;
		StringBuilder sb = new StringBuilder();
		while ((lineText = bufReader.readLine()) != null) {
			sb.append(lineText);
		}
		return sb.toString();
        //return getMethod.getResponseBodyAsString(); 
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
	/**
	 * 下载网络图片到本地
	 * @param imgUrl 网络图片路径
	 * @param folderPath 本地存放目录
	 * @param fileName 存放的文件名
	 * */
	public void downImage(final String imgUrl,final String folderPath,final String fileName) {
		System.out.println("开始下载图片:" + imgUrl);
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		pool.execute(new Runnable() {
			@Override
			public void run() {
				HttpClient client = HttpUtil.getHttpClient();
				GetMethod get = new GetMethod(imgUrl);
				try {
					int status_code = client.executeMethod(get);
					if(status_code == HttpStatus.SC_OK){
						byte[] data = readFromResponse(get);
						String savePaht = folderPath + fileName;
				        File imageFile = new File(savePaht);  
				        FileOutputStream outStream = new FileOutputStream(imageFile);  
				        outStream.write(data);  
				        outStream.close();  
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					get.releaseConnection();
				}
			}
		});
	}
	public static byte[] readFromResponse(GetMethod get) throws Exception{ 
		InputStream inStream = get.getResponseBodyAsStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        long length = get.getResponseContentLength();
        //显示文件大小格式：2个小数点显示
    	DecimalFormat df = new DecimalFormat("0.00");
    	//进度条下面显示的总文件大小
    	String fileSize = df.format((float) length / 1024 / 1024) + "MB";
        byte[] buffer = new byte[1024];  
        int len = 0;
        
        //int count = 0;
        //String processText;
        long t = System.currentTimeMillis();
        
        while((len = inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);
            //下载进度
            //count += len;
            //processText = df.format((float) count / 1024 / 1024) + "MB" + "/" + fileSize;
            //System.out.println(processText);
        }  
        System.out.println("下载完成，耗时：" + (System.currentTimeMillis() - t) + "毫秒");
        inStream.close();  
        return outStream.toByteArray();  
    }
}
