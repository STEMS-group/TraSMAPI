package trasmapi.sumo;

import java.io.IOException;
import java.util.ArrayList;

import trasmapi.genAPI.Edge;
import trasmapi.genAPI.exceptions.WrongCommand;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;
import trasmapi.sumo.protocol.ResponseMessage;

public class SumoEdge extends Edge {

	public SumoEdge(String id) {
		this.id = id;
		this.vehicleIdList = new ArrayList<String>();
	}

	public static ArrayList<String> getEdgeIdList() {
        Command cmd = new Command(Constants.CMD_GET_EDGE_VARIABLE);
        Content cnt = new Content(Constants.ID_LIST, "0");

        cmd.setContent(cnt);
        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte)Constants.CMD_GET_EDGE_VARIABLE,
                    (byte)Constants.RESPONSE_GET_EDGE_VARIABLE,
                    (byte)Constants.ID_LIST,
                    (byte)Constants.TYPE_STRINGLIST);

            return content.getStringList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongCommand e) {
            e.printStackTrace();
        }
        return null;
	}

	public double getGlobalTravelTime(int time){

		Command cmd = new Command(Constants.CMD_GET_EDGE_VARIABLE);

		Content cnt = new Content(Constants.VAR_EDGE_TRAVELTIME,id,Constants.TYPE_INTEGER);

		cnt.setInteger(time);
		
		cmd.setContent(cnt);

		//cmd.print("Command getTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			
		//	rspMsg.print();
			
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_EDGE_VARIABLE, (byte)  Constants.RESPONSE_GET_EDGE_VARIABLE,
					(byte)  Constants.VAR_EDGE_TRAVELTIME, (byte)  Constants.TYPE_DOUBLE);


			edgeTravelTime = content.getDouble();

			return edgeTravelTime;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return edgeTravelTime = -1;
	}

	public double getCurrentTime(){

		Command cmd = new Command(Constants.CMD_GET_EDGE_VARIABLE);

		Content cnt = new Content(Constants.VAR_CURRENT_TRAVELTIME,id);

		cmd.setContent(cnt);

		//cmd.print("Command getTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			
		//	rspMsg.print();
			
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_EDGE_VARIABLE, (byte)  Constants.RESPONSE_GET_EDGE_VARIABLE,
					(byte)  Constants.VAR_CURRENT_TRAVELTIME, (byte)  Constants.TYPE_DOUBLE);


			currentTravelTime = content.getDouble();

			return currentTravelTime;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return currentTravelTime = -1;
		
	}


	public void setGlobalTravelTime(int beginTime, int endTime, double travelTimeValue){

		Command cmd = new Command(Constants.CMD_SET_EDGE_VARIABLE);
		
		Content cnt = new Content(Constants.VAR_EDGE_TRAVELTIME,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, beginTime));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, endTime));
		items.add(new Pair<Integer,Object>(Constants.TYPE_DOUBLE, travelTimeValue));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

		cmd.print("setGlobalTravelTime");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);

			if(rspMsg.status.getResult() != 0){
				System.out.println("setGlobalTravelTime ERROR, edge : "+id);
			}
			
		} catch (IOException e) {
			System.out.println("Receiving setGlobalTravelTime change Status");
			e.printStackTrace();
		}
	}
	
   public int getNumVehicles() {
        Command cmd = new Command(Constants.CMD_GET_EDGE_VARIABLE);
        Content cnt = new Content(Constants.LAST_STEP_VEHICLE_NUMBER, id);

        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte)Constants.CMD_GET_EDGE_VARIABLE,
                    (byte)Constants.RESPONSE_GET_EDGE_VARIABLE,
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
}
