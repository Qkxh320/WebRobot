package me.yongbo.robot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yongbo.bean.Meitu91Image;
import me.yongbo.bean.Meitu91Response;
import me.yongbo.robot.util.DbHelper;
import me.yongbo.robot.util.HttpUtil;

import com.google.gson.Gson;

public class Meitu91Robot extends WebRobot {
	
	private final static String HOST = "www.91meitu.net";
	private final static String REFERER = "http://www.91meitu.net/";
	private final static String POINT_URL = "http://www.91meitu.net/img-item/get-next?1&lastid=%1$d";

	private int startIndex;
	private int endIndex;
	
	
	private Gson gson;
	
	private boolean doAgain = true;
	
	protected DbHelper dbHelper;
	public Meitu91Robot(int startIndex){
		this(startIndex, -1);
	}
	
	public Meitu91Robot(int startIndex,int endIndex){
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.dbHelper = new DbHelper();
		gson = new Gson();
	}
	
	public static Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		param.put("X-Requested-With", "XMLHttpRequest");
		param.put("DK_AJAX_REQUEST", "ajax-reqeust");
		return param;
	}
	
	public List<Meitu91Image> doWork() {
		String rp;
		List<Meitu91Image> imgs = null;
		try {
			rp = getResponseString(String.format(POINT_URL, startIndex));
			Meitu91Response response = gson.fromJson(rp, Meitu91Response.class);
			if(response.getCount() != 0) {
				startIndex = response.getLastId();
				imgs = response.getImages();
				//写入数据库
				//dbHelper.execute("saveImage", response.getImages());
			} else {
				doAgain = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imgs;
	}

	@Override
	public void run() {
		while (doAgain) {
			if(endIndex != -1 && startIndex > endIndex){
				break;
			}
			doWork();
		}
		System.out.println(startIndex);
	}
}
