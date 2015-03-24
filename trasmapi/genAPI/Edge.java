package trasmapi.genAPI;

import java.util.ArrayList;

import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class Edge {
	public String id;
	public ArrayList<String> vehicleIdList;

	public double edgeTravelTime;
	public double currentTravelTime;

	
	/**
	 * method used to retrieve the String id of the Edge
	 * @return the edge id
	 */
	public String getId() {
		return id;
	}

	public double getGlobalTravelTime(int time) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	public double getCurrentTime() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public double getMeanSpeed() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void setGlobalTravelTime(int beginTime, int endTime, double travelTimeValue) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
}
