package com.clock;


public class PicBackList {
	PicBack data[];
	
	public PicBackList() {
		data = new PicBack[] {
				new PicBack(R.drawable.line_r, R.drawable.point_r, "one"),
				new PicBack(R.drawable.line_r2, R.drawable.point_r2, "two")};
	}
	public PicBack[] getData(){
		return data;
	}
	
	public String getName(int backID, int tickID){
		for (int i = 0; i < data.length; i++) {
			if (data[i].getTickID() == tickID && data[i].getBackID() == backID) {
				return data[i].getBackText();
			}
		}
		return "";
	}
	
}
