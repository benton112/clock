package com.clock;

public class Pic {

    private int picId;
    private String picText;
    public Pic(){
        super();
    }
    
    public Pic(int picId, String title) {
        super();
        this.picId = picId;
        this.picText = title;
    }
    public int getPicId(){
    	return picId;
    }
    public String getPicText(){
    	return picText;
 
    }
}