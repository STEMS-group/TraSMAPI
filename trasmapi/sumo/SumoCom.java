package trasmapi.sumo;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trasmapi.genAPI.Polygon;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.WrongCommand;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;
import trasmapi.sumo.protocol.ResponseMessage;
import trasmapi.sumo.protocol.SubscriptionResponse;
import trasmapi.sumo.protocol.Variable;

public class SumoCom {	

	private boolean debug = false;
	private boolean debug1 = false;

	//Sumo connection
	public Socket socket;
	public String server;
	public int port;

	public Process proc;
	public List<String> params;

	static DataOutputStream out;
	static DataInputStream in;


	public static int currentSimStep;
	public static ArrayList<String> routesIDs = new ArrayList<String>();
	public static ArrayList<String> edgesIDs = new ArrayList<String>();
	public static ArrayList<String> vehicleTypesIDs = new ArrayList<String>();

	public static ArrayList<SumoVehicle> vehicles = new ArrayList<SumoVehicle>();
	private static ArrayList<SumoPolygon> polygons = new ArrayList<SumoPolygon>();
	public static ArrayList<SumoRoute> routes = new ArrayList<SumoRoute>();

	private static ArrayList<SumoEdge> edges = new ArrayList<SumoEdge>();

	private static ArrayList<String> loadedVehicles = new ArrayList<String>();
	private static ArrayList<String> departedVehicles = new ArrayList<String>();
	public static ArrayList<String> arrivedVehicles = new ArrayList<String>();

	private static int simStartStep = 0;
	private static int simEndStep = 10000000;

	public SumoCom(){
		routesIDs = new ArrayList<String>();

		vehicles = new ArrayList<SumoVehicle>();
		polygons = new ArrayList<SumoPolygon>();
		routes = new ArrayList<SumoRoute>();

		edges = new ArrayList<SumoEdge>();

		loadedVehicles = new ArrayList<String>();
		departedVehicles = new ArrayList<String>();
		arrivedVehicles = new ArrayList<String>();

	}

	public void launch(String simulator) {

		try {
			List<String> command = new ArrayList<String>();
			String OS = System.getProperty("os.name").toLowerCase();
			if (simulator == "sumo") {
				if(OS.indexOf("win") >= 0)
				{
					command.add("sumo.exe");
				} else { //Assumed Unix based
					command.add("sumo");
				}
				command.addAll(params);
			} else if (simulator == "guisim") {
				if(OS.indexOf("win") >= 0){
					command.add("sumo-gui.exe");
				} else { //Assumed Unix based
					command.add("sumo-gui");
				}
				command.addAll(params);
			}
			proc = new ProcessBuilder(command).start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void connect() throws TimeoutException {

		boolean connected = false;
		//give sumo time to startup, don't give up on first try
		for (int i=1;i<10;i++) {
			try {
				socket = new Socket(server, port);
				SumoCom.out = new DataOutputStream(socket.getOutputStream());
				SumoCom.in = new DataInputStream(socket.getInputStream());
				connected = true;
				break;
			}
			catch (IOException e) {
				try {
					Thread.sleep(i*1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		if (!connected) {
			proc.destroy();
			throw new TimeoutException();
		}

		try {
			socket.setKeepAlive(true);
		} catch (SocketException e) {
			System.out.println("Unknown SocketException raised when tried to keep alive.");
			e.printStackTrace();
		}	
	}

	public void close() throws IOException {

		Command cmd = new Command(Constants.CMD_CLOSE);

		if(debug) cmd.print("Close");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {
			reqMsg.sendRequestMessage(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.flush();

		out.close();
		in.close();
		socket.close();
		proc.destroy();

	}

	public synchronized boolean simStep(int k) {

		try {

			Command cmd = new Command(Constants.CMD_SIMSTEP2);

			Content cnt = new Content(k);

			cmd.setContent(cnt);

			if(debug) cmd.print("SimStep");

			RequestMessage reqMsg = new RequestMessage();
			reqMsg.addCommand(cmd);

			ResponseMessage response = query(reqMsg);

			//	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			//	response.print();

			if(response.status.getResult() == Constants.RTYPE_OK)
				parseSubscriptions(response);

		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Exiting simulation...");
			return false;
		}
		return true;
	}

	private void parseSubscriptions(ResponseMessage response) {

		for(Command cmd: response.commands)
			parseCommand(cmd);
	}

	private void parseCommand(Command cmd) {

		if(cmd.content instanceof SubscriptionResponse){
			if(debug1) System.out.println("Is SubscriptionResponse");
			switch((byte)cmd.cmdId){
			case (byte) Constants.RESPONSE_SUBSCRIBE_SIM_VARIABLE:
				parseSimulationVariables((SubscriptionResponse)(cmd.content));
			break;
			case (byte) Constants.RESPONSE_SUBSCRIBE_VEHICLE_VARIABLE:
				parseVehicleVariables((SubscriptionResponse)(cmd.content));
			break;
			case (byte) Constants.RESPONSE_SUBSCRIBE_EDGE_VARIABLE:
				parseEdgeVariables((SubscriptionResponse)(cmd.content));
			break;
			}
		}
		else{
			if(debug1) System.out.println("Not SubscriptionResponse");
		}
	}

	private void parseEdgeVariables(SubscriptionResponse subscriptionResponse) {

		for(SumoEdge edge: edges)
			if(edge.id.equals(subscriptionResponse.objectId)){

				if(debug1) System.out.println("** E.id : "+ edge.id);
				edge.vehicleIdList = new ArrayList<String>();

				for(Variable v: subscriptionResponse.variables){
					switch(v.varId){
					case Constants.LAST_STEP_VEHICLE_ID_LIST:
						edge.vehicleIdList = v.getStringList();
						break;
					}
				}		
			}
	}

	private void parseVehicleVariables(SubscriptionResponse subscriptionResponse) {

		for(SumoVehicle vehicle: vehicles)
			if(vehicle.id.equals(subscriptionResponse.objectId)){
				if(debug1) System.out.println("** V.id : "+ vehicle.id);
				vehicle.alive = true;
				for(Variable v: subscriptionResponse.variables){
					switch(v.varId){
					case Constants.VAR_SPEED:
						vehicle.speed = v.getDouble(0);
						if(debug1) System.out.println("** speed : "+ vehicle.speed);
						break;
					case Constants.VAR_POSITION:
						vehicle.position = v.getPosition();
						if(debug1) System.out.println("** position : "+ vehicle.position);
						break;
					case Constants.VAR_ANGLE:
						vehicle.angle = v.getDouble(0);
						if(debug1) System.out.println("** angle : "+ vehicle.angle);
						break;
					case Constants.VAR_ROAD_ID:
						vehicle.edgeId = v.getString();
						if(debug1) System.out.println("** edgeId : "+ vehicle.edgeId);
						break;
					case Constants.VAR_LANE_ID:
						vehicle.laneId = v.getString();
						if(debug1) System.out.println("** laneId : "+ vehicle.laneId);
						break;
					case Constants.VAR_LANE_INDEX:
						vehicle.laneIndex = v.getInt(0);
						if(debug1) System.out.println("** laneIndex : "+ vehicle.laneIndex);
						break;
					case Constants.VAR_TYPE:
						vehicle.typeId = v.getString();
						if(debug1) System.out.println("** typeId : "+ vehicle.typeId);
						break;
					case Constants.VAR_ROUTE_ID:
						vehicle.routeId = v.getString();
						if(debug1) System.out.println("** routeId : "+ vehicle.routeId);
						break;
					case Constants.VAR_EDGES:
						vehicle.edges = v.getStringList();
						if(debug1) System.out.println("** edges : "+ vehicle.edges);
						break;
					case Constants.VAR_COLOR:
						vehicle.color = v.getColor();
						if(debug1) System.out.println("** color : "+ vehicle.color);
						break;
					case Constants.VAR_LANEPOSITION:
						vehicle.lanePosition = v.getDouble(0);
						if(debug1) System.out.println("** lanePosition : "+ vehicle.lanePosition);
						break;
					case Constants.VAR_SIGNALS:
						vehicle.signalState = v.getInt(0);
						if(debug1) System.out.println("** signalState : "+ vehicle.signalState);
						break;
					default:
						if(debug1) System.out.println("NOT IMPLEMENTED VARIABLE " + Integer.toString(v.varId & 0xFF, 16) +" in parseVehicleVariables@SumoCom");
						break;



					}
				}		
			}
	}

	private void parseSimulationVariables(SubscriptionResponse subscription) {
		for(Variable v: subscription.variables){
			switch(v.varId){
			case Constants.VAR_TIME_STEP:
				setCurrentSimStep(v.getInt(0));
				break;
			case Constants.VAR_LOADED_VEHICLES_IDS:
				loadLoadedVehicles(v.getStringList());
				break;
			case Constants.VAR_DEPARTED_VEHICLES_IDS:
				loadDepartedVehicles(v.getStringList());
				break;
				/*	case Constants.VAR_TELEPORT_STARTING_VEHICLES_IDS:
			loadTeleportedStartVehicles(v.getStringList());
		case Constants.VAR_TELEPORT_ENDING_VEHICLES_IDS:
			loadTeleportedEndedVehicles(v.getStringList());*/
			case Constants.VAR_ARRIVED_VEHICLES_IDS:
				loadArrivedVehicles(v.getStringList());
				break;
			}
		}
	}

	private void loadArrivedVehicles(ArrayList<String> arrivedVehiclesIDs) {

		//System.out.println("Arrived : " + arrivedVehiclesIDs);

		for(String s: arrivedVehiclesIDs){
			for(SumoVehicle v: vehicles)
				if(v.id.equals(s)){
					v.arrived = true;
					v.alive = false;
					v.arrivalTime = currentSimStep;
				}
		}
	}

	private void loadDepartedVehicles(ArrayList<String> stringList) {

		for(String s: stringList)
			if(!departedVehicles.contains(s)){
				departedVehicles.add(s);
				for(SumoVehicle v: vehicles)
					if(v.id.equals(s))
						v.departed = true;
			}
	}

	private void loadLoadedVehicles(ArrayList<String> stringList) {

		for(String s : stringList)
			if(!loadedVehicles.contains(s)){
				loadedVehicles.add(s);
				vehicles.add(new SumoVehicle(s));
			}
	}

	public synchronized static ResponseMessage query(RequestMessage request) throws IOException {

		request.sendRequestMessage(out);

		return new ResponseMessage(in);
	}
	
	public static ArrayList<String> getAllEdgesIds() {

		Command cmd = new Command(Constants.CMD_GET_EDGE_VARIABLE);

		Content cnt = new Content(Constants.ID_LIST, "dummy");

		cmd.setContent(cnt);

		//cmd.print("Command getAllEdgesIds");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {
			ResponseMessage rspMsg = query(reqMsg);
			Content content = rspMsg.validate((byte)Constants.CMD_GET_EDGE_VARIABLE,  (byte)Constants.RESPONSE_GET_EDGE_VARIABLE,
					(byte)Constants.ID_LIST,  (byte)Constants.TYPE_STRINGLIST);

			
			if(rspMsg.status.getResult() == 0){
				edgesIDs = content.getStringList();
				return edgesIDs;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static ArrayList<String> getAllVehiclesIds() {

		Command cmd = new Command(Constants.CMD_GET_VEHICLE_VARIABLE);

		Content cnt = new Content(Constants.ID_LIST, "dummy");

		cmd.setContent(cnt);

		//cmd.print("Command getAllVeh");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {

			ResponseMessage rspMsg = query(reqMsg);
			Content content = rspMsg.validate((byte)Constants.CMD_GET_VEHICLE_VARIABLE,  (byte)Constants.RESPONSE_GET_VEHICLE_VARIABLE,
					(byte)Constants.ID_LIST,  (byte)Constants.TYPE_STRINGLIST);

			return content.getStringList();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<String> getAllVehiclesTypesIds() {

		Command cmd = new Command(Constants.CMD_GET_VEHICLETYPE_VARIABLE);

		Content cnt = new Content(Constants.ID_LIST, "dummy");

		cmd.setContent(cnt);

		//cmd.print("Command getAllVehiclesTypesIds");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {

			ResponseMessage rspMsg = query(reqMsg);
			Content content = rspMsg.validate((byte)Constants.CMD_GET_VEHICLETYPE_VARIABLE,  (byte)Constants.RESPONSE_GET_VEHICLETYPE_VARIABLE,
					(byte)Constants.ID_LIST,  (byte)Constants.TYPE_STRINGLIST);

			return content.getStringList();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void subscribeAll() {

		subscribeSimulationValues();
	}

	private static void subscribeSimulationValues() {

		Command cmd = new Command(Constants.CMD_SUBSCRIBE_SIM_VARIABLE);

		ArrayList<Integer> variableList = new ArrayList<Integer>();
		variableList.add(Constants.VAR_TIME_STEP);
		//variableList.add(Constants.VAR_LOADED_VEHICLES_IDS);
		//variableList.add(Constants.VAR_DEPARTED_VEHICLES_IDS);
		//variableList.add(Constants.VAR_TELEPORT_STARTING_VEHICLES_IDS);
		//variableList.add(Constants.VAR_TELEPORT_ENDING_VEHICLES_IDS);
		variableList.add(Constants.VAR_ARRIVED_VEHICLES_IDS);

		Content cnt = new Content(simStartStep,simEndStep,"dummy",variableList);

		cmd.setContent(cnt);

		//cmd.print("Command CurrentSimulationTime");

		RequestMessage reqMsg = new RequestMessage();

		reqMsg.addCommand(cmd);

		try {

			query(reqMsg);

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static ArrayList<String> getAllRoutesIds() {

		Command cmd = new Command(Constants.CMD_GET_ROUTE_VARIABLE);

		Content cnt = new Content(Constants.ID_LIST, "dummy");

		cmd.setContent(cnt);

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {

			ResponseMessage rspMsg = query(reqMsg);

			Content content = rspMsg.validate((byte)Constants.CMD_GET_ROUTE_VARIABLE,  (byte)Constants.RESPONSE_GET_ROUTE_VARIABLE,
					(byte)Constants.ID_LIST,  (byte)Constants.TYPE_STRINGLIST);

			if(content.getStringList().size() == 0)
				return null;
			return content.getStringList();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return null;

	}

	public void start(int i) {
		simStep(i);

		loadRoutes();
		
		vehicleTypesIDs = getAllVehiclesTypesIds();

		subscribeAll();
	}

	private static void loadRoutes() {

		routesIDs = getAllRoutesIds();

		if(routesIDs == null){
			//<System.out.println("NULL routesIds");
			return;
		}

		System.out.println("numIds : " + routesIDs.size());

		RequestMessage reqMsg = new RequestMessage();

		for(String s: routesIDs){

			Command cmd = new Command(Constants.CMD_GET_ROUTE_VARIABLE);
			Content cnt = new Content(Constants.VAR_EDGES, s);

			cmd.setContent(cnt);

			reqMsg.addCommand(cmd);
		}

		try {

			ResponseMessage rspMsg = query(reqMsg);

			for(Command c: rspMsg.commands){

				SumoRoute newRoute = new SumoRoute(c.content.id);
				newRoute.setEdges(c.content.getStringList());

				routes.add(newRoute);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void subscribeVehicle(String id) {

		Command cmd = new Command(Constants.CMD_SUBSCRIBE_VEHICLE_VARIABLE);

		ArrayList<Integer> variableList = new ArrayList<Integer>();
		variableList.add(Constants.VAR_SPEED);
		variableList.add(Constants.VAR_POSITION);
		variableList.add(Constants.VAR_ANGLE);
		variableList.add(Constants.VAR_ROAD_ID);
		variableList.add(Constants.VAR_LANE_ID);
		variableList.add(Constants.VAR_LANE_INDEX);
		variableList.add(Constants.VAR_TYPE);
		variableList.add(Constants.VAR_ROUTE_ID);
		variableList.add(Constants.VAR_EDGES);
		variableList.add(Constants.VAR_COLOR);
		variableList.add(Constants.VAR_LANEPOSITION);
		variableList.add(Constants.VAR_SIGNALS);

		Content cnt = new Content(simStartStep,simEndStep,id,variableList);

		cmd.setContent(cnt);

		//	cmd.print("  --- Command SubscribeVehicle id: "+id);

		RequestMessage reqMsg = new RequestMessage();

		reqMsg.addCommand(cmd);

		try {

			query(reqMsg);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void subscribeContexPolygon(SumoPolygon polygon) {

		Command cmd = new Command(0x88);

		int contextDomain = Constants.CMD_GET_VEHICLE_VARIABLE;

		ArrayList<Integer> variableList = new ArrayList<Integer>();
		variableList.add(Constants.VAR_SPEED);

		Content cnt = new Content(simStartStep,simEndStep,polygon.id,contextDomain,polygon.sensingRadius,variableList);

		cmd.setContent(cnt);

		//	cmd.print("  --- Command subscribeContexPolygon id: "+polygon.id);

		RequestMessage reqMsg = new RequestMessage();

		reqMsg.addCommand(cmd);

		try {

			query(reqMsg);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void subscribeEdgeVehicles(SumoEdge actionEdge) {

		Command cmd = new Command(Constants.CMD_SUBSCRIBE_EDGE_VARIABLE);

		ArrayList<Integer> variableList = new ArrayList<Integer>();
		variableList.add(Constants.LAST_STEP_VEHICLE_ID_LIST);

		Content cnt = new Content(simStartStep,simEndStep,actionEdge.id,variableList);

		cmd.setContent(cnt);

		//	cmd.print("  --- Command subscribeEdgeVehicles id: "+actionEdge.id);

		RequestMessage reqMsg = new RequestMessage();

		reqMsg.addCommand(cmd);

		try {

			query(reqMsg);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	public static synchronized SumoVehicle newSumoVehicle(String vType, String routeId, int departTime, double departPosition, int departLane) {

		SumoVehicle newVehicle = new SumoVehicle(vType, routeId, departTime, departPosition, 0.0 , (byte) departLane);

		newVehicle.addCommand();

		vehicles.add(newVehicle);

		return newVehicle;
	}
	 */
	public static Polygon newSumoPolygon(double x, double y, Color color) {

		SumoPolygon newPolygon = new SumoPolygon(x, y, color);

		polygons.add(newPolygon);

		return newPolygon;
	}

	public static SumoEdge getEdge(String edgeId) {

		SumoEdge newEdge = new SumoEdge(edgeId);

		edges.add(newEdge);

		return newEdge;
	}

	public static int getCurrentSimStep() {
		return currentSimStep;
	}

	public void setCurrentSimStep(int currentSimStepP) {
		currentSimStep = currentSimStepP;
	}

	public static String getRouteId(String from, String to) {

		if(to == null)
			for(SumoRoute r: routes){
				if(r.edges.get(0).id.equals(from))
					return r.id;
			}

		else
			for(SumoRoute r: routes)
				if(r.edges.get(0).id.equals(from) && r.edges.get(r.edges.size()-1).id.equals(to))
					return r.id;

		return null;
	}

	public static HashMap<String, Integer> getNumVehiclesPerRouteID() {

		HashMap<String, Integer> vehPerRoute = new HashMap<String, Integer>();

		for(SumoVehicle v: vehicles){
			String route = v.routeId;

			if(!vehPerRoute.containsKey(route))
				vehPerRoute.put(route, 1);
			else
				vehPerRoute.put(route, vehPerRoute.get(route)+1);
		}
		return vehPerRoute;
	}

	public static void addAllVehiclesToSimulation() {

		int vehAdded = 1;

		for(SumoVehicle v: vehicles){

			Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);

			Content cnt = new Content((byte)Constants.ADD,v.id,Constants.TYPE_COMPOUND);

			ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();

			items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, v.typeId));
			items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, v.routeId));
			items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, v.departTime));
			items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, v.departPosition));
			items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, v.departSpeed));
			items.add(new Pair<Integer,Object>(Constants.TYPE_BYTE, v.departLane));

			cnt.setCompound(items);

			cmd.setContent(cnt);

			RequestMessage reqMsg = new RequestMessage();
			reqMsg.addCommand(cmd);

			try {

				ResponseMessage rspMsg = query(reqMsg);

				if(rspMsg.status.getResult() != 0)
					System.out.println("ADD VEHICLE ERROR!");
				else
					System.out.println("ADDED " + (vehAdded++) + " VEHICLES!");


			} catch (IOException e) {
				System.out.println("Receiving addAllVehicles Status");
				e.printStackTrace();
			}
		}

	}

	public static void addVehicleToSimulation(SumoVehicle v) {

		Command cmd = new Command(Constants.CMD_SET_VEHICLE_VARIABLE);

		Content cnt = new Content((byte)Constants.ADD,v.id,Constants.TYPE_COMPOUND);

		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();

		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, v.typeId));
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, v.routeId));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, v.departTime));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, v.departPosition));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, v.departSpeed));
		items.add(new Pair<Integer,Object>(Constants.TYPE_BYTE, v.departLane));

		cnt.setCompound(items);

		cmd.setContent(cnt);

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {

			ResponseMessage rspMsg = query(reqMsg);

			if(rspMsg.status.getResult() != 0)
				System.out.println("ADD VEHICLE ERROR!");
			else
				System.out.println("ADDED VEHICLE!");


		} catch (IOException e) {
			System.out.println("Receiving addAllVehicles Status");
			e.printStackTrace();
		}

	}

	public synchronized static void addVehicle(SumoVehicle vehicle) {

		vehicles.add(vehicle);
	}

	//Transforms all edges in a route, so that every departure Edge(in a route) is a known route.
	public static void createAllRoutes() {

		getAllEdgesIds();
		
		removeInternalEdges();
		
		System.out.println(edgesIDs);
		
		int routeId = 1;
		for(String edgeId: edgesIDs){
			
			SumoRoute route = new SumoRoute(routeId+"");
			route.edges.add(new SumoEdge(edgeId));
			
			route.addRoute();
			
			routeId++;
		}
		
		loadRoutes();
		//System.out.println(edgesIds.size());
	//	System.out.println(routes.size());
	}

	private static void removeInternalEdges() {

		ArrayList<String> newEdges = new ArrayList<String>();

		for(String s: edgesIDs)
			if(!s.contains(":"))
				newEdges.add(s);
		
		edgesIDs = newEdges;
	}

	public static SumoVehicle getVehicleById(int vId) {

		for(SumoVehicle v: vehicles)
			if(v.id.equals(vId+""))
				return v;
		return null;
	}
}
