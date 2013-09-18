package me.yongbo.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yongbo.robot.bean.HuabanImage;
import me.yongbo.robot.bean.HuabanPin;
import me.yongbo.robot.bean.HuabanResponse;
import me.yongbo.robot.bean.MyImage;
import me.yongbo.robot.dbhelper.ImageDbHelper;
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

	private int pageSize = 20; // 每次请求默认加载数据的条数

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
		this.dbHelper = new ImageDbHelper();
	}

	private Boolean isOkImageType(String type) {
		if (type.equalsIgnoreCase("image/jpeg")
				|| type.equalsIgnoreCase("image/gif")
				|| type.equalsIgnoreCase("image/png")
				|| type.equalsIgnoreCase("image/jpg")) {
			return true;
		}
		return false;
	}

	private List<MyImage> handlerData(List<HuabanPin> hps) {
		HuabanImage img = null;
		List<MyImage> mImgs = new ArrayList<MyImage>();
		for (HuabanPin hp : hps) {
			img = hp.getFile();
			String imgUrl = String.format(IMG_HOST, img.getKey());
			if (_DEBUG) {
				System.out.println(imgUrl); // 调试代码
			}
			if (isOkImageType(img.getType())) {
				String fileType = "."
						+ img.getType().substring(
								img.getType().indexOf("/") + 1);

				img.setId(hp.getPin_id());
				img.setImgUrl(imgUrl);
				img.setObjType(objtype);
				img.setType("image/" + fileType.substring(1));
				mImgs.add(img); // 转化为统一图片类型
			}
		}
		// 写入数据库
		if (databaseEnable) {
			dbHelper.execute("saveImage", mImgs);
		}
		return mImgs;
	}

	public List<MyImage> doWork() {
		String rp = getResponseString(String.format(POINT_URL, category,
				maxPinId, pageSize));
		List<MyImage> imgs = new ArrayList<MyImage>();
		if (rp.isEmpty()) {
			return imgs;
		}
		// System.out.println(rp);
		HuabanResponse response = gson.fromJson(rp, HuabanResponse.class);
		List<HuabanPin> hps = response.getPins();

		if (hps.size() == 0) {
			doAgain = false;
			return imgs;
		}
		maxPinId = hps.get(hps.size() - 1).getPin_id();
		imgs = handlerData(hps);
		return imgs;
	}

	@Override
	public void run() {
		while (doAgain) {
			doWork();
		}
		shutdownRobot();
	}

	public static Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		param.put("X-Request", "JSON");
		param.put("X-Requested-With", "XMLHttpRequest");
		return param;
	}
}
