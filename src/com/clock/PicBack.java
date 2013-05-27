package com.clock;

public class PicBack {

    private int backID;
    private int tickID;
    private String text;

    public PicBack(){
        super();
    }
    
    public PicBack(int backID, int tickID, String text) {
        super();
        this.backID = backID;
        this.tickID = tickID;
        this.text = text;
    }
    public int getBackID(){
    	return backID;
    }
    public int getTickID(){
    	return tickID;
 
    }
    public String getBackText(){
    	return text;
 
    }
}