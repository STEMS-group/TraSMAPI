package trasmapi.sumo;

import trasmapi.genAPI.LanePosition;

public class SumoLanePosition extends LanePosition {
	public String lane;
	public float pos;
	
	public SumoLanePosition(String lane, float pos) {
		this.lane = lane;
		this.pos = pos;
	}
}
