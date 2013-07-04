package me.yongbo.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import me.yongbo.bean.Meitu91Response;
import me.yongbo.robot.util.HttpUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

import com.google.gson.Gson;

public class Meitu91Robot extends WebRobot {
	
	private final static String HOST = "www.91meitu.net";
	private final static String REFERER = "http://www.91meitu.net/";
	private final static String POINT_URL = "http://www.91meitu.net/img-item/get-next?1&lastid=%1$d";
	
	
	
	private int startIndex;
	private int endIndex;
	
	private HttpClient httpClient;
	private GetMethod getMethod;
	
	private Gson gson;
	
	private boolean doAgain = true;
	public Meitu91Robot(int startIndex){
		this(startIndex, -1);
	}
	
	public Meitu91Robot(int startIndex,int endIndex){
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.httpClient = HttpUtil.getHttpClient();
		this.getMethod = HttpUtil.getHttpGet(getRequestHeaders());
		gson = new Gson();
	}
	
	private Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		param.put("X-Requested-With", "XMLHttpRequest");
		param.put("DK_AJAX_REQUEST", "ajax-reqeust");
		return param;
	}

	public String getResponseString(String url) throws Exception {
		getMethod.setURI(new URI(url));
		httpClient.executeMethod(getMethod);
		BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), "UTF-8"));
        String s; 
        StringBuilder sb = new StringBuilder("");
        while ((s = reader.readLine()) != null) {
            sb.append(s);
        }
        reader.close();
        getMethod.releaseConnection();
        System.out.println(sb.toString());
        return sb.toString(); 
	}
	
	public void doWork() throws Exception {
		String rp = getResponseString(String.format(POINT_URL, startIndex));
		Meitu91Response response = gson.fromJson(rp, Meitu91Response.class);
		if(response.getCount() != 0) {
			startIndex = response.getLastId();
			System.out.println(startIndex);
		} else {
			doAgain = false;
		}
	}

	@Override
	public void run() {
		while (doAgain) {
			if(endIndex != -1 && startIndex > endIndex){
				break;
			}
			try {
				doWork();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
