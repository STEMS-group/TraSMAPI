package trasmapi.sumo;

import java.util.ArrayList;
import trasmapi.genAPI.InductionLoop;

public class SumoInductionLoop extends InductionLoop {
	
	//the position of the induction loop at it's lane, counted from the lane's begin, in meters.
	double position;
	
	// the ID of the lane the induction loop is placed at.
	String laneId;
	
	ArrayList<SumoVehicle> vehicleIds;
	
	
	/**
	 * @param name - the id of the induction loop
	 */
	public SumoInductionLoop(String name) {
		this.id = name;
		vehicleIds = new ArrayList<SumoVehicle>() ;
	}
		
	/**
	 * method used to retrieve the number of vehicles that "touched" the induction loop in the last induction loop read cycle
	 * @return number of vehicles
	 */
	public int getNumVehicles() {
		return 0;
		
	}
	
	
	/**
	 * method used to retrieve all the vehicles that were on the induction loop during the last simulation step 
	 * @return SumoVehicle[] - a SumoVehicle array with all the Vehicles
	 */
	public SumoVehicle[] getVehiclesList() {
		return null;

	}

	/**
	 * 
	 * @return last step occupancy (%) of the induction loop
	 */
	public double getOccupancy() {
		return 0;
		
	}
	
	/**
	 * 
	 * @return position of the induction loop at its lane
	 */
	public double getPosition() {
		return 0;
		
	}
	
	/**
	 * 
	 * @return time since last detection
	 */
	public double getTimeSinceLastDetection() {
		return 0;
		
	}
	
	/**
	 * 
	 * @return mean velocity of (all) the vehicles passing over this induction loop in the last simulation step
	 */
	public double getMeanVelocity() {
		return 0;
		
	}
}
