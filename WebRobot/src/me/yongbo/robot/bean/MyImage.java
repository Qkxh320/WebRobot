package me.yongbo.robot.bean;

import java.util.HashMap;
import java.util.Map;

public class MyImage extends MyEntity {
	
	protected String id;
	protected String savePath;
	protected String imgUrl;
	protected int width;
	protected int height;
	protected String type;
	protected int objType; //1： 妹子  2：宠物 
	
	public static Map<String, Integer> OBJ_TYPE = new HashMap<String, Integer>();
	
	static {
		OBJ_TYPE.put("meinv", 1);
		OBJ_TYPE.put("beauty", 1);
		OBJ_TYPE.put("pets", 2);
		OBJ_TYPE.put("kids", 3);
	}
	
	public int getObjType() {
		return objType;
	}
	public void setObjType(int objType) {
		this.objType = objType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
