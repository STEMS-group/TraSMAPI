package trasmapi.sumo;

import trasmapi.genAPI.Link;

public class SumoLink extends Link{

	String from;
	String to;
	String across;
	
	public void setFrom(String str) {
		this.from = str;
	}

	public void setTo(String str) {
		this.to = str;
	}

	public void setAcross(String str) {
		this.across = str;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getAcross() {
		return across;
	}

}
