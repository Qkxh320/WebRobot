package me.yongbo.robot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yongbo.bean.Meitu91Image;
import me.yongbo.bean.Meitu91Response;
import me.yongbo.robot.util.DbHelper;
import me.yongbo.robot.util.HttpUtil;

import com.google.gson.Gson;

public class Meitu91Robot extends WebRobot {
	
	public final static String default_img_savedir = "F:"+ File.separator + "webimage" + File.separator;
	
	private final static String IMG_HOST = "http://meitu91.b0.upaiyun.com/%1$s";
	private final static String HOST = "www.91meitu.net";
	private final static String REFERER = "http://www.91meitu.net/";
	private final static String POINT_URL = "http://www.91meitu.net/img-item/get-next?1&lastid=%1$d";

	private int startIndex;
	private int endIndex;
	
	
	private Gson gson;
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
	private void handlerData(List<Meitu91Image> imgs){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String imgUrl;
		String folderPath = default_img_savedir + sdf.format(date) + File.separator;
		for (Meitu91Image img : imgs) {
			imgUrl = String.format(IMG_HOST, img.getFilename());
			String fileType = imgUrl.substring(imgUrl.lastIndexOf(".") - 1);
			downImage(imgUrl, folderPath, sdf.format(date) + img.getId() + fileType);
		}
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
				handlerData(imgs);
				//System.out.println(response.getLastId());
				//写入数据库
				//dbHelper.execute("saveImage", response.getImages());
			} else {
				doAgain = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			doAgain = false;
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
