package trasmapi.genAPI;

import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class Statistics {
	
	protected boolean tlflag;
	protected boolean ilflag;
	protected boolean laneflag;
	
	public Statistics() {
		tlflag = false;
		ilflag = false;
		laneflag = false;
	}
	
	public void enableAll() {
		tlflag = true;
		ilflag = true;
		laneflag = true;
	}
	
	public void enableTrafficLights() {
		tlflag = true;
	}
	
	public void enableInductionLoops() {
		ilflag = true;
	}
	
	public void enableLanes() {
		laneflag = true;
	}
	
	public void update() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	public Integer getTLTroughput(String id) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	public Integer getTLTroughput(String id, String lane) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	public Integer getTLTroughput(Integer index, String id) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	public Integer getTLTroughput(Integer index, String id, String laneId) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
}
