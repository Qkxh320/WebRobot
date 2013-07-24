package me.yongbo.bean;

import java.util.List;
public class HuabanResponse extends Entity {
	private List<HuabanPin> pins;

	public List<HuabanPin> getPins() {
		return pins;
	}

	public void setPins(List<HuabanPin> pins) {
		this.pins = pins;
	}
	
}
