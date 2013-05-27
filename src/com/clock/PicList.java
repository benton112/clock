package com.clock;

public class PicList {
	Pic data[];
	public PicList() {
		data = new Pic[] { new Pic(R.drawable.fig_r, "TrollFace"),
				new Pic(R.drawable.fig_r1, "Baby"),
				new Pic(R.drawable.fig_r2, "MageBaby"),
				new Pic(R.drawable.fig_r_, "Car"), };
	}
	public Pic[] getData(){
		return data;
	}
	
	public String getName(int picID){
		for (int i = 0; i < data.length; i++) {
			if (data[i].getPicId() == picID) {
				return data[i].getPicText();
			}
		}
		return "";
	}
	
}
