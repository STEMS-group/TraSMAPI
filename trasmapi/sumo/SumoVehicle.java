package trasmapi.sumo;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import trasmapi.genAPI.Route;
import trasmapi.genAPI.Vehicle;
import trasmapi.genAPI.exceptions.WrongCommand;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;
import trasmapi.sumo.protocol.ResponseMessage;

public class SumoVehicle extends Vehicle {
	
	public static ArrayList<Command> vehicleCommands = new ArrayList<Command>();
	
	/**
	 * creates a new SumoVehicle, associated with the corresponding vehicle with id "id" in the simulator
	 * @param id the simulation vehicle id
	 **/
	public SumoVehicle(String id) {
		super(id);
	}
	
	public SumoVehicle(String vType, String routeId2, int departTime, double departPosition, double departSpeed,byte departLane) {
		super(classId+"");
		classId++;
		
		this.typeId = vType;
		this.routeId = routeId2;
		this.departTime = departTime;
		this.departPosition = departPosition;
		this.departSpeed = departSpeed;
		this.departLane = departLane;
	}

	public SumoVehicle(String vehicleType, String routeId2, int departureTime,
			int departPosition, byte departLane) {
		super(classId+"");
		classId++;
		
		this.typeId = vehicleType;
		this.routeId = routeId2;
		this.departTime = departureTime;
		this.departPosition = departPosition;
		this.departLane = departLane;
	}

	public SumoVehicle(int vehicleId, String vehicleType, String routeId2, int departureTime,
			int departPosition, byte departLane) {
		super(vehicleId+"");
		
		this.typeId = vehicleType;
		this.routeId = routeId2;
		this.departTime = departureTime;
		this.departPosition = departPosition;
		this.departSpeed = 0.0;
		this.departLane = departLane;
		
	}

	public SumoVehicle(int id, String vehicleType, String routeId, int departureTime, double departPosition, double departSpeed, byte departLane) {
		super(id+"");
		
		this.typeId = vehicleType;
		this.routeId = routeId;
		this.departTime = departureTime;
		this.departPosition = departPosition;
		this.departSpeed = departSpeed;
		this.departLane = departLane;
	}

	public void addVehicle() {

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content((byte)Constants.ADD,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, typeId));
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, routeId));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, departTime));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, departPosition));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, departSpeed));
		items.add(new Pair<Integer,Object>(Constants.TYPE_BYTE, departLane));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

		//cmd.print("AddVehicle");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			
			
			if(rspMsg.status.getResult() != 0)
				System.out.println("ADD VEHICLE \"" + id + "\" ERROR!");
			else
				System.out.println("ADDED VEHICLE \"" + id);
				
			
		} catch (IOException e) {
			System.out.println("Receiving AddVehicle change Status");
			e.printStackTrace();
		}
		
	}
	
	public synchronized void addCommand() {

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content((byte)Constants.ADD,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, typeId));
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, routeId));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, departTime));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, departPosition));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, departSpeed));
		items.add(new Pair<Integer,Object>(Constants.TYPE_BYTE, departLane));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

		vehicleCommands.add(cmd);
	}

	public double getSpeed(){
		
		Command cmd = new Command(Constants.CMD_GET_VEHICLE_VARIABLE);
		Content cnt = new Content(Constants.VAR_SPEED,id);
		
		cmd.setContent(cnt);
		
		//cmd.print("Command");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_VEHICLE_VARIABLE, (byte)  Constants.RESPONSE_GET_VEHICLE_VARIABLE,
					 (byte)  Constants.VAR_SPEED, (byte)  Constants.TYPE_DOUBLE);
			
			speed = content.getDouble();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}
		
		return speed;
	}

	public void setSpeed(double speedP) {
		
		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_SPEED , id , Constants.TYPE_DOUBLE);
		cnt.setDouble(speedP);
		
		cmd.setContent(cnt);

		//cmd.print("Command");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() == 0){
				this.speed = speedP;
			}
			
		} catch (IOException e) {
			System.out.println("Receiving Speed set Status");
			e.printStackTrace();
		}
		
		
		this.speed = speedP;
	}

	public void setMaxSpeed(double newMaxSpeed){
		
		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_MAXSPEED, id, Constants.TYPE_DOUBLE);
		cnt.setDouble(newMaxSpeed);
		
		cmd.setContent(cnt);
		
		//cmd.print("SetMaxSpeed");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			
			if(rspMsg.status.getResult() == 0){
				this.maxSpeed = newMaxSpeed;
			}
			
		} catch (IOException e) {
			System.out.println("Receiving MaxSpeed set Status");
			e.printStackTrace();
		}
	}

	public void stop(String edge, double position, byte lane, int duration){
		
		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.CMD_STOP,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, edge));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, position));
		items.add(new Pair<Integer,Object>(Constants.TYPE_BYTE, lane));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, duration));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

		//cmd.print("SetStop");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("Stop ERROR, vehicle : "+id);
			}
			
		} catch (IOException e) {
			System.out.println("Receiving SetStop change Status");
			e.printStackTrace();
		}
	}

	public void setLane(byte laneId, int duration){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);

		Content cnt = new Content(Constants.CMD_CHANGELANE,id,Constants.TYPE_COMPOUND);

		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_BYTE, laneId));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, duration));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

		//cmd.print("changeLane");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("LaneChange ERROR, vehicle : "+id);
			}
			//resp.print();
			
		} catch (IOException e) {
			System.out.println("Receiving changeLane change Status");
			e.printStackTrace();
		}
	}

	public void slowDown(double newSpeed, int newSpeedTime){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);

		Content cnt = new Content(Constants.CMD_SLOWDOWN,id,Constants.TYPE_COMPOUND);

		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, newSpeed));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, newSpeedTime));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

		//cmd.print("slowDown");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("SlowDown ERROR, vehicle : "+id);
			}
			//resp.print();
			
		} catch (IOException e) {
			System.out.println("Receiving slowDown change Status");
			e.printStackTrace();
		}
	}

	public synchronized void changeTarget(String destinationEdge){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);

		Content cnt = new Content(Constants.CMD_CHANGETARGET,id,Constants.TYPE_STRING);

		cnt.setString(destinationEdge);
		
		cmd.setContent(cnt);

		//cmd.print("changeTarget");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			
			if(rspMsg.status.getResult() == 0)
				this.destiny = destinationEdge;
			//resp.print();
			
		} catch (IOException e) {
			System.out.println("Receiving changeTarget change Status");
			e.printStackTrace();
		}
		
	}

	public void setColor(Color color){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);

		Content cnt = new Content(Constants.VAR_COLOR,id,Constants.TYPE_COLOR);

		cnt.setColor(color);
		
		cmd.setContent(cnt);

		//cmd.print("changeColor");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() == 0)
				this.color = color;
			//resp.print();
			
		} catch (IOException e) {
			System.out.println("Receiving changeColor change Status");
			e.printStackTrace();
		}
		
	}

	public void setRouteById(Route newRoute){
		
		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);

		Content cnt = new Content(Constants.VAR_ROUTE,id,Constants.TYPE_STRING);

		cnt.setString(newRoute.id);
		
		cmd.setContent(cnt);

		//cmd.print("setRouteById");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() == 0){
				this.route = newRoute;
				this.routeId = newRoute.id;
			}
			//resp.print();
			
		} catch (IOException e) {
			System.out.println("Receiving setRouteById change Status");
			e.printStackTrace();
		}
		
	}

	public String getRouteId() {
		
		Command cmd = new Command(Constants.CMD_GET_VEHICLE_VARIABLE);
		Content cnt = new Content(Constants.VAR_ROUTE,id);
		
		cmd.setContent(cnt);
		
		cmd.print("Command GetRoute");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_VEHICLE_VARIABLE, (byte)  Constants.RESPONSE_GET_VEHICLE_VARIABLE,
					 (byte)  Constants.VAR_ROUTE, (byte)  Constants.TYPE_STRING);
			
			routeId = content.getString();

			return routeId;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return routeId;
	}

	public double getEdgeEffort(String edgeId, int time){

		Command cmd = new Command(Constants.CMD_GET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_EDGE_EFFORT,id,Constants.TYPE_COMPOUND);
		
		
		ArrayList<Pair<Integer, Object>> items = new ArrayList<Pair<Integer,Object>>();

		items.add(new Pair<Integer, Object>(Constants.TYPE_INTEGER, time));
		items.add(new Pair<Integer, Object>(Constants.TYPE_STRING, edgeId));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);
		
		//cmd.print("Command getEdgeEffort");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_VEHICLE_VARIABLE, (byte)  Constants.RESPONSE_GET_VEHICLE_VARIABLE,
					 (byte)  Constants.VAR_EDGE_EFFORT, (byte)  Constants.TYPE_DOUBLE);
			
			//rspMsg.print();
			
			edgeEffort = content.getDouble();

			return edgeEffort;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return edgeEffort;
	}
	
	public synchronized double getTravelTime(String edgeId, int time){

		Command cmd = new Command(Constants.CMD_GET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_EDGE_TRAVELTIME,id,Constants.TYPE_COMPOUND);
		
		
		ArrayList<Pair<Integer, Object>> items = new ArrayList<Pair<Integer,Object>>();

		items.add(new Pair<Integer, Object>(Constants.TYPE_INTEGER, time));
		items.add(new Pair<Integer, Object>(Constants.TYPE_STRING, edgeId));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);
		
		//cmd.print("Command getTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_VEHICLE_VARIABLE, (byte)  Constants.RESPONSE_GET_VEHICLE_VARIABLE,
					 (byte)  Constants.VAR_EDGE_TRAVELTIME, (byte)  Constants.TYPE_DOUBLE);
			
		//	rspMsg.print();
			
			edgeTravelTime = content.getDouble();

			return edgeTravelTime;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return edgeTravelTime;
	}

	public synchronized ArrayList<String> getEdges() {

		Command cmd = new Command(Constants.CMD_GET_VEHICLE_VARIABLE);
		Content cnt = new Content(Constants.VAR_EDGES,id);
		
		cmd.setContent(cnt);
		
		//cmd.print("Command GETEDGES");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_VEHICLE_VARIABLE, (byte)  Constants.RESPONSE_GET_VEHICLE_VARIABLE,
					 (byte)  Constants.VAR_EDGES, (byte)  Constants.TYPE_STRINGLIST);
			
			edges = content.getStringList();

			return edges;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return null;
	}

	public synchronized void rerouteByTravelTime(){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.CMD_REROUTE_TRAVELTIME,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

	//	cmd.print("rerouteByTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("rerouteByTravelTime ERROR, vehicle : "+id);
			}
			else
				System.out.println("rerouteByTravelTime, vehicle : "+id);
			
		} catch (IOException e) {
			System.out.println("Receiving rerouteByTravelTime change Status");
			e.printStackTrace();
		}
	}

	public void setEdgeTravelTime(int beginTime, int endTime, String edgeID, double value){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_EDGE_TRAVELTIME,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();

		items.add(new Pair<Integer, Object>(Constants.TYPE_INTEGER, beginTime));
		items.add(new Pair<Integer, Object>(Constants.TYPE_INTEGER, endTime));
		items.add(new Pair<Integer, Object>(Constants.TYPE_STRING, edgeID));
		items.add(new Pair<Integer, Object>(Constants.TYPE_DOUBLE, value));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

	//	cmd.print("setEdgeTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		
		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("setEdgeTravelTime ERROR, vehicle : "+id);
			}
			else
				System.out.println("setEdgeTravelTime, vehicle : "+id);
			
		} catch (IOException e) {
			System.out.println("Receiving setEdgeTravelTime change Status");
			e.printStackTrace();
		}
	}

	public void setEdgeTravelTime(String edgeID, double value){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_EDGE_TRAVELTIME,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();

		items.add(new Pair<Integer, Object>(Constants.TYPE_STRING, edgeID));
		items.add(new Pair<Integer, Object>(Constants.TYPE_DOUBLE, value));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

	//	cmd.print("setEdgeTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("setEdgeTravelTime ERROR, vehicle : "+id);
			}
			else
				System.out.println("setEdgeTravelTime, vehicle : "+id);
			
		} catch (IOException e) {
			System.out.println("Receiving setEdgeTravelTime change Status");
			e.printStackTrace();
		}
	}

	public void setEdgeTravelTime(String edgeId2){

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_EDGE_TRAVELTIME,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();

		items.add(new Pair<Integer, Object>(Constants.TYPE_STRING, edgeId2));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

	//	cmd.print("setEdgeTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("setEdgeTravelTime ERROR, vehicle : "+id);
			}
			else
				System.out.println("setEdgeTravelTime, vehicle : "+id);
			
		} catch (IOException e) {
			System.out.println("Receiving setEdgeTravelTime change Status");
			e.printStackTrace();
		}
	}

	public String getEdgeId(){

		Command cmd = new Command(Constants.CMD_GET_VEHICLE_VARIABLE);
		Content cnt = new Content(Constants.VAR_ROAD_ID,id);
		
		cmd.setContent(cnt);
		
		//cmd.print("Command");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_VEHICLE_VARIABLE, (byte)  Constants.RESPONSE_GET_VEHICLE_VARIABLE,
					 (byte)  Constants.VAR_ROAD_ID, (byte)  Constants.TYPE_STRING);
			
			edgeId = content.getString();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}
		
		return edgeId;
	}

	public void focus(){
		
		Command cmd = new Command(Constants.CMD_SET_GUI_VARIABLE);

		Content cnt = new Content(Constants.VAR_TRACK_VEHICLE,"View #0",Constants.TYPE_STRING);

		cnt.setString(id);
		
		cmd.setContent(cnt);

	//	cmd.print("setRouteById");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() == 0){
			}
			//resp.print();
			
		} catch (IOException e) {
			System.out.println("Receiving setRouteById change Status");
			e.printStackTrace();
		}
	}

	public void unFocus(){

		Command cmd = new Command(Constants.CMD_SET_GUI_VARIABLE);

		Content cnt = new Content(Constants.VAR_TRACK_VEHICLE,"View #0",Constants.TYPE_STRING);

		cnt.setString("dummy");
		
		cmd.setContent(cnt);

	//	cmd.print("setRouteById");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() == 0){
			}
			//resp.print();
			
		} catch (IOException e) {
			System.out.println("Receiving setRouteById change Status");
			e.printStackTrace();
		}
	}
	
	public void setSignalState(){

//		byte emergency = 11000001000000;
//		
//		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);
//
//		Content cnt = new Content(Constants.VAR_MAXSPEED, id, Constants.TYPE_DOUBLE);
//		cnt.setDouble(newMaxSpeed);
//
//		cmd.setContent(cnt);
//
//		cmd.print("SetMaxSpeed");
//
//		RequestMessage reqMsg = new RequestMessage();
//		reqMsg.addCommand(cmd);
//
//
//		try {
//
//			ResponseMessage rspMsg = SumoCom.query(reqMsg);
//
//			if(rspMsg.status.getResult() == 0){
//				this.maxSpeed = newMaxSpeed;
//			}
//
//		} catch (IOException e) {
//			System.out.println("Receiving MaxSpeed set Status");
//			e.printStackTrace();
//		}
	}
}
