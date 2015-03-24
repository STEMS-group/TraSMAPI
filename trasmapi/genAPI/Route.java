package trasmapi.genAPI;

import java.util.ArrayList;

import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class Route {

	public String id;

	public Route() {
	}
	
	public Route(String routeId) {
		id = routeId;
	}

	public void setEdges(ArrayList<String> newStringList) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
}
