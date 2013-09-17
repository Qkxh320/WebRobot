package me.yongbo.robot.bean;

import java.util.List;

public class TouTiaoResponse {
	private String message;
	private List<TouTiaoObj> data;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<TouTiaoObj> getData() {
		return data;
	}
	public void setData(List<TouTiaoObj> data) {
		this.data = data;
	} 
	
}
