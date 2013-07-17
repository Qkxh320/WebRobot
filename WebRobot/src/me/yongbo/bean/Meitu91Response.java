package me.yongbo.bean;

import java.util.List;
public class Meitu91Response extends Entity {
	private List<Meitu91Image> images;
	private int lastId;
	private boolean status;
	private String message;
	private int count;
	public List<Meitu91Image> getImages() {
		return images;
	}
	public void setImages(List<Meitu91Image> images) {
		this.images = images;
	}
	public int getLastId() {
		return lastId;
	}
	public void setLastId(int lastId) {
		this.lastId = lastId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
