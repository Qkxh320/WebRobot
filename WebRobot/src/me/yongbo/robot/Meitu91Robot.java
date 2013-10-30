package me.yongbo.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yongbo.robot.bean.Meitu91Image;
import me.yongbo.robot.bean.Meitu91Response;
import me.yongbo.robot.bean.MyImage;
import me.yongbo.robot.dbhelper.ImageDbHelper;
import me.yongbo.robot.util.HttpUtil;

public class Meitu91Robot extends WebRobot {

	public static String rootDir = "D:/wakao/webimage/mt91image/";
	private final static String BEFORE = "91mt_";

	private final static String IMG_HOST = "http://meitu91.b0.upaiyun.com/%1$s";
	private final static String HOST = "www.91meitu.net";
	private final static String REFERER = "http://www.91meitu.net/";
	private final static String POINT_URL = "http://www.91meitu.net/img-item/get-next?1&lastid=%1$d";

	private int startIndex;
	private int endIndex;
	private int objtype;

	
	protected ImageDbHelper dbHelper;

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
		this(startIndex, endIndex, false);
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
		this.dbHelper = new ImageDbHelper();
		this.objtype = MyImage.OBJ_TYPE.get("meinv");
		
	}

	public static Map<String, String> getRequestHeaders() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("Referer", REFERER);
		param.put("Host", HOST);
		param.put("X-Requested-With", "XMLHttpRequest");
		param.put("DK_AJAX_REQUEST", "ajax-reqeust");
		return param;
	}

	private List<MyImage> handlerData(List<Meitu91Image> imgs) {
		List<MyImage> mImgs = new ArrayList<MyImage>();
		
		for (Meitu91Image img : imgs) {
			String imgUrl = String.format(IMG_HOST, img.getFileName());
			String fileType = imgUrl.substring(imgUrl.lastIndexOf(".") - 1);
			img.setImgUrl(imgUrl);
			img.setObjType(objtype);
			img.setType("image/" + fileType.substring(1));
			mImgs.add(img); //转化为统一图片类型
			
			initSaveDir(rootDir);
			//System.out.println(folderPath+img.getFileName().substring(img.getFileName().lastIndexOf("/")+1));
			downImage(imgUrl, folderPath, img.getFileName().substring(img.getFileName().lastIndexOf("/")+1));
		}
		// 写入数据库
		if (databaseEnable) {
			dbHelper.execute("saveImage", mImgs);
		}
		return mImgs;
	}

	public List<MyImage> doWork() {
		String rp = getResponseString(String.format(POINT_URL, startIndex));
		if(rp == null){ 
			return null;
		}
		
		Meitu91Response response = gson.fromJson(rp, Meitu91Response.class);
		List<MyImage> imgs = new ArrayList<MyImage>();
		if (response.getCount() != 0) {
			startIndex = response.getLastId();
			imgs = handlerData(response.getImages());
			
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
		shutdownRobot();
	}
}
