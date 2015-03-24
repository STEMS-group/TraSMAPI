package trasmapi.genAPI;

import java.awt.Color;
import java.util.ArrayList;

import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.vartypes.Position2D;

public class Vehicle implements Comparable<Vehicle> {	

	public static int classId = 0;
	
	public String id;
	public boolean alive = false;
	public double maxSpeed;
	
	public Route route;
	public boolean departed;
	public boolean arrived = false;
	
	public String destiny;
	public String edgeId;
	public double speed;
	public Position2D position;
	public double angle;
	public String laneId;
	public int laneIndex;
	public String typeId;
	public String routeId;
	public ArrayList <String> edges;
	public Color color;
	public double lanePosition;
	public int signalState;
	public double CO2emission;
	public double COemission;
	public int departTime;
	public int arrivalTime;
	public double departPosition;
	public double departSpeed;
	public byte departLane;
	public double edgeTravelTime;
	public double edgeEffort;
	
	public Vehicle(String idDriver) {
		id = idDriver;
	}

	public void rerouteByTravelTime() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	public void setMaxSpeed(double newMaxSpeed) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	/**
	 * standard comparison method, lexicographical by id
	 * @param a the other Vehicle
	 * @return wheter the 2 vehicles are equal
	 */
	public boolean equals(Vehicle a) {
		return id.equals(a.id);
	}
	
	/**
	 * method used to check wheter a vehicle is inside the simulator
	 * @return returns true if the vehicle is currently inside the simulation, false otherwise
	 * @throws UnimplementedMethod
	 */
	public boolean inSimulation() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * standard comparison method, sorts lexicographically by its string id
	 */
	public int compareTo(Vehicle a) {
		return id.compareTo(a.id);
	}

	public void stop(String edge, double position, byte laneId, int duration) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void changeLane(byte laneId, int duration) throws UnimplementedMethod {
		throw new UnimplementedMethod();
		
	}

	public void slowDown(double newSpeed, int newSpeedTime) throws UnimplementedMethod {
		throw new UnimplementedMethod();
		
	}

	public void changeTarget(String destinationEdge) throws UnimplementedMethod {
		throw new UnimplementedMethod();
		
	}

	public void focus() throws UnimplementedMethod {
		throw new UnimplementedMethod();
		
	}

	public void unFocus() throws UnimplementedMethod {
		throw new UnimplementedMethod();
		
	}

	public void setRouteById(Route newRoute) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the alive
	 */
	public boolean isAlive() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the edgeId
	 */
	public String getEdgeId() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param edgeId the edgeId to set
	 */
	public void setEdgeId(String edgeId) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the position
	 */
	public Position2D getPosition() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position2D position) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the angle
	 */
	public double getAngle() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(double angle) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the laneId
	 */
	public String getLaneId() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param laneId the laneId to set
	 */
	public void setLaneId(String laneId) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the laneIndex
	 */
	public int getLaneIndex() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param laneIndex the laneIndex to set
	 */
	public void setLaneIndex(int laneIndex) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the typeId
	 */
	public String getTypeId() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the routeId
	 */
	public String getRouteId() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param routeId the routeId to set
	 */
	public void setRouteId(String routeId) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the edges
	 */
	public ArrayList<String> getEdges() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(ArrayList<String> edges) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the signalState
	 */
	public int getSignalState() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param signalState the signalState to set
	 */
	public void setSignalState(int signalState) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the cO2emission
	 */
	public double getCO2emission() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param cO2emission the cO2emission to set
	 */
	public void setCO2emission(double cO2emission) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the cOemission
	 */
	public double getCOemission() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param cOemission the cOemission to set
	 */
	public void setCOemission(double cOemission) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the departed
	 */
	public boolean isDeparted() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param departed the departed to set
	 */
	public void setDeparted(boolean departed) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the arrived
	 */
	public boolean isArrived() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param arrived the arrived to set
	 */
	public void setArrived(boolean arrived) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the departTime
	 */
	public int getDepartTime() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param departTime the departTime to set
	 */
	public void setDepartTime(int departTime) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the departPosition
	 */
	public double getDepartPosition() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param departPosition the departPosition to set
	 */
	public void setDepartPosition(double departPosition) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the departSpeed
	 */
	public double getDepartSpeed() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param departSpeed the departSpeed to set
	 */
	public void setDepartSpeed(double departSpeed) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the departLane
	 */
	public byte getDepartLane() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param departLane the departLane to set
	 */
	public void setDepartLane(byte departLane) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param lanePosition the lanePosition to set
	 */
	public void setLanePosition(double lanePosition) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(Route route) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the color
	 */
	public Color getColor() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the id
	 */
	public String getId() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the lanePosition
	 */
	public double getLanePosition() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * @return the route
	 */
	public Route getRoute() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public double getTravelTime(String edgeId, int time) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public double getEdgeEffort(String edgeId, int time) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void setEdgeTravelTime(int beginTime, int endTime, String edgeID, double value) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void setEdgeTravelTime(String edgeId2, double i) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void setEdgeTravelTime(String edgeId2) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
}
