package me.yongbo.bean;

public class HuabanPin extends Entity {
	private String pin_id;
	private HuabanImage file;

	public String getPin_id() {
		return pin_id;
	}

	public void setPin_id(String pin_id) {
		this.pin_id = pin_id;
	}

	public HuabanImage getFile() {
		return file;
	}

	public void setFile(HuabanImage file) {
		this.file = file;
	}
	
}
