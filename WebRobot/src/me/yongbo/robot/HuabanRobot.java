package me.yongbo.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import me.yongbo.bean.HuabanImage;
import me.yongbo.bean.HuabanPin;
import me.yongbo.bean.HuabanResponse;
import me.yongbo.bean.MyImage;
import me.yongbo.dbhelper.ImageDbHelper;
import me.yongbo.robot.util.HttpUtil;

public class HuabanRobot extends WebRobot {
	
	public static String rootDir = "D:/wakao/webimage/hbimage/";
	private final static String BEFORE = "hb_";

	private final static String HOST = "huaban.com";
	private final static String IMG_HOST = "http://img.hb.aicdn.com/%1$s_fw580";
	private final static String REFERER = "http://huaban.com/";
	private final static String POINT_URL = "http://huaban.com/favorite/%1$s/?hji68jpd&max=%2$s&limit=%3$d&wfl=1";

	private String maxPinId;
	private String category;
	private int objtype;
	
	private int pageSize = 20; //每次请求默认加载数据的条数

	private Gson gson;
	protected ImageDbHelper dbHelper;

	public HuabanRobot(String maxPinId, String category) {
		this(maxPinId, category, false);
	}
	public HuabanRobot(String maxPinId, String category, Boolean databaseEnable) {
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.maxPinId = maxPinId;
		this.category = category;
		this.databaseEnable = databaseEnable;
		this.objtype = MyImage.OBJ_TYPE.get(category);
		this.gson = new Gson();
	}

	private Boolean isOkImageType(String type) {
		Boolean isImage = true;
		switch (type) {
		case "image/jpeg":
			break;
		case "image/gif":
			break;
		case "image/png":
			break;
		case "image/jpg":
			break;
		default:
			isImage = false;
		}
		return isImage;
	}

	private List<MyImage> handlerData(List<HuabanPin> hps) {
		initSaveDir(rootDir);
		HuabanImage img = null;
		List<MyImage> mImgs = new ArrayList<MyImage>();
		for (HuabanPin hp : hps) {
			img = hp.getFile();
			String imgUrl = String.format(IMG_HOST, img.getKey());
			//System.out.println(imgUrl);
			if (isOkImageType(img.getType())) {
				String fileType = "."
						+ img.getType().substring(
								img.getType().indexOf("/") + 1);
				String fileName = BEFORE + hp.getPin_id() + fileType;
				
				img.setId(hp.getPin_id());
				img.setImgUrl(imgUrl);
				img.setSavePath(curDir + fileName);
				img.setObjType(objtype);
				
				//System.out.println(img.getSavePath());
				mImgs.add(img); //转化为统一图片类型
				//下载图片到本地
				//downImage(imgUrl, folderPath, fileName);
			}
		}
		return mImgs;
	}

	public void doWork() {
		String rp = getResponseString(String.format(POINT_URL, category, maxPinId, pageSize));
		//System.out.println(rp);
		
		HuabanResponse response = gson.fromJson(rp, HuabanResponse.class);
		List<HuabanPin> hps = response.getPins();
		
		if(hps.size() == 0){
			doAgain = false ;
			return;
		}
		maxPinId = hps.get(hps.size() - 1).getPin_id();
		
		List<MyImage> mImgs = handlerData(hps);
		
		// 写入数据库
		if (databaseEnable) {
			//dbHelper.execute("saveImage", mImgs);
		}
	}

	@Override
	public void run() {
		while (doAgain) {
			doWork();
		}
	}

	public static Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		param.put("X-Request", "JSON");
		param.put("X-Requested-With", "XMLHttpRequest");
		return param;
	}
}
