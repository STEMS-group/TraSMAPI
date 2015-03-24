package trasmapi.sumo;

import java.io.IOException;
import java.util.ArrayList;

import trasmapi.genAPI.Lane;
import trasmapi.genAPI.exceptions.WrongCommand;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;
import trasmapi.sumo.protocol.ResponseMessage;

public class SumoLane extends Lane {
	
	float length;
	
	public SumoLane(String id) {
		this.id = id;
		loadLength();
	}
	
	public float getLength() {
        return length;
    }
	
	public void setMaxSpeed(float val) {
		
	}
	
	/**
	 * method used to retrieve all the lane ids in the loaded network
	 * @return String[] - a String[] with all the lane ids
	 */
	public static String[] getLaneIdList() {
		return null;
		
	}
	
	public String getEdgeId() {
        Command cmd = new Command(Constants.CMD_GET_LANE_VARIABLE);
        Content cnt = new Content(Constants.LANE_EDGE_ID, id);

        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte)Constants.CMD_GET_LANE_VARIABLE,
                    (byte)Constants.RESPONSE_GET_LANE_VARIABLE,
                    (byte)Constants.LANE_EDGE_ID,
                    (byte)Constants.TYPE_STRING);

            return content.getString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongCommand e) {
            e.printStackTrace();
        }
        return null;
	}

	/**
	 * returns a list of the vehicles in this lane
	 * @return the list of vehicles in this lane
	 */
	public SumoVehicle[] vehiclesList() {
        Command cmd = new Command(Constants.CMD_GET_LANE_VARIABLE);
        Content cnt = new Content(Constants.LAST_STEP_VEHICLE_ID_LIST, id);

        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte)Constants.CMD_GET_LANE_VARIABLE,
                    (byte)Constants.RESPONSE_GET_LANE_VARIABLE,
                    (byte)Constants.LAST_STEP_VEHICLE_ID_LIST,
                    (byte)Constants.TYPE_STRINGLIST);

            ArrayList<SumoVehicle> vehicles = new ArrayList<SumoVehicle>();
            for (String vehicleId: content.getStringList()) {
                vehicles.add(new SumoVehicle(vehicleId));
            }
            return vehicles.toArray(new SumoVehicle[vehicles.size()]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongCommand e) {
            e.printStackTrace();
        }
        return null;		
	}
	
	/**
	 * returns the number of vehicles stopped in this Lane
	 * @param minVel - threshold velocity to be considered stopped
	 * @return number of stopped vehicles
	 */
	public int getNumVehiclesStopped(Double minVel) {
		return 0;
	
	}
	
	/**
	 * returns the number of vehicles in this Lane
	 * @return number of stopped vehicles
	 */
	public int getNumVehicles() {
        Command cmd = new Command(Constants.CMD_GET_LANE_VARIABLE);
        Content cnt = new Content(Constants.LAST_STEP_VEHICLE_NUMBER, id);

        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte)Constants.CMD_GET_LANE_VARIABLE,
                    (byte)Constants.RESPONSE_GET_LANE_VARIABLE,
                    (byte)Constants.LAST_STEP_VEHICLE_NUMBER,
                    (byte)Constants.TYPE_INTEGER);

            return content.getInteger();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongCommand e) {
            e.printStackTrace();
        }
        return -1;
	}
	
	public boolean equals(SumoLane s) {
		return this.id.equals(s.id);
	}
	
	public String toString() {
		return id;
	}

	public void loadLength() {
        Command cmd = new Command(Constants.CMD_GET_LANE_VARIABLE);
        Content cnt = new Content(Constants.VAR_LENGTH, id);

        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte)Constants.CMD_GET_LANE_VARIABLE,
                    (byte)Constants.RESPONSE_GET_LANE_VARIABLE,
                    (byte)Constants.VAR_LENGTH,
                    (byte)Constants.TYPE_DOUBLE);

            length = (float) content.getDouble();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongCommand e) {
            e.printStackTrace();
        }
	}
}
