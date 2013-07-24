package me.yongbo.robot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yongbo.bean.Meitu91Image;
import me.yongbo.bean.Meitu91Response;
import me.yongbo.dbhelper.Meitu91DbHelper;
import me.yongbo.robot.util.HttpUtil;

import com.google.gson.Gson;

public class Meitu91Robot extends WebRobot {

	public static String rootDir = "D:/wakao/webimage/mt91image/";
	private final static String BEFORE = "91mt_";

	private final static String IMG_HOST = "http://meitu91.b0.upaiyun.com/%1$s";
	private final static String HOST = "www.91meitu.net";
	private final static String REFERER = "http://www.91meitu.net/";
	private final static String POINT_URL = "http://www.91meitu.net/img-item/get-next?1&lastid=%1$d";

	private int startIndex;
	private int endIndex;

	private Gson gson;
	protected Meitu91DbHelper dbHelper;

	/**
	 * 构造函数
	 * 
	 * @param startIndex
	 *            开始位置
	 * */
	public Meitu91Robot(int startIndex) {
		this(startIndex, -1, false);
	}

	/**
	 * 构造函数
	 * 
	 * @param startIndex
	 *            开始位置
	 * @param databaseEnable
	 *            是否写入数据库
	 * */
	public Meitu91Robot(int startIndex, Boolean databaseEnable) {
		this(startIndex, -1, databaseEnable);
	}

	/**
	 * 构造函数
	 * 
	 * @param startIndex
	 *            开始位置
	 * @param endIndex
	 *            结束位置
	 * */
	public Meitu91Robot(int startIndex, int endIndex) {
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.databaseEnable = false;
		this.dbHelper = new Meitu91DbHelper();
		gson = new Gson();
	}

	/**
	 * 构造函数
	 * 
	 * @param startIndex
	 *            开始位置
	 * @param endIndex
	 *            结束位置
	 * @param databaseEnable
	 *            是否写入数据库
	 * */
	public Meitu91Robot(int startIndex, int endIndex, Boolean databaseEnable) {
		super(HttpUtil.getHttpGet(getRequestHeaders()));
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.databaseEnable = databaseEnable;
		this.dbHelper = new Meitu91DbHelper();
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

	private void handlerData(List<Meitu91Image> imgs) {
		initSaveDir(rootDir);
		for (Meitu91Image img : imgs) {
			String imgUrl = String.format(IMG_HOST, img.getFileName());
			String fileType = imgUrl.substring(imgUrl.lastIndexOf(".") - 1);
			String fileName = BEFORE + img.getId() + fileType;
			// System.out.println(imgUrl);
			img.setImgUrl(imgUrl);
			img.setSavePath(curDir + fileName);
			downImage(imgUrl, folderPath, fileName);
		}
		// 写入数据库
		if (databaseEnable) {
			dbHelper.execute("saveImage", imgs);
		}
	}

	public List<Meitu91Image> doWork() {
		String rp;
		List<Meitu91Image> imgs = null;
		rp = getResponseString(String.format(POINT_URL, startIndex));
		Meitu91Response response = gson.fromJson(rp, Meitu91Response.class);
		if (response.getCount() != 0) {
			startIndex = response.getLastId();
			handlerData(response.getImages());
		} else {
			doAgain = false;
		}
		return imgs;
	}

	@Override
	public void run() {
		while (doAgain) {
			if (endIndex != -1 && startIndex > endIndex) {
				break;
			}
			doWork();
		}
		System.out.println("数据抓取完毕....");
	}
}
