package com.sfebiz.supplychain.protocol.kd100;

import java.io.Serializable;

public class KD100CallbackItem implements Serializable {
	
	private static final long serialVersionUID = -1744686637466550881L;

    private String context;
	
    private String time;
	
    private String ftime;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	
}
