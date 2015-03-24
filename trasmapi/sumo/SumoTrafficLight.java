package trasmapi.sumo;

import java.io.IOException;
import java.util.ArrayList;



import java.util.List;

import trasmapi.genAPI.TrafficLight;
import trasmapi.genAPI.exceptions.WrongCommand;
import trasmapi.sumo.SumoTrafficLightProgram.Phase;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;
import trasmapi.sumo.protocol.ResponseMessage;

public class SumoTrafficLight extends TrafficLight {

	String state;
	private ArrayList<String> controlledLanes;
	
	public SumoTrafficLight(String id) {
		super(id);
	}

	/**	 
	 * @return ArrayList<String> - all Traffic Lights ids
	 */
	public static ArrayList<String> getIdList(){

		Command cmd = new Command(Constants.CMD_GET_TL_VARIABLE);

		Content cnt = new Content(Constants.ID_LIST, "dummy");

		cmd.setContent(cnt);

		//cmd.print("Command getAllVeh");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {

			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate((byte)Constants.CMD_GET_TL_VARIABLE,  (byte)Constants.RESPONSE_GET_TL_VARIABLE,
					(byte)Constants.ID_LIST,  (byte)Constants.TYPE_STRINGLIST);

			return content.getStringList();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getState() {

		Command cmd = new Command(Constants.CMD_GET_TL_VARIABLE);
		Content cnt = new Content(Constants.TL_RED_YELLOW_GREEN_STATE,id);

		cmd.setContent(cnt);

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);


		try {

			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_TL_VARIABLE, (byte)  Constants.RESPONSE_GET_TL_VARIABLE,
					(byte)  Constants.TL_RED_YELLOW_GREEN_STATE, (byte)  Constants.TYPE_STRING);

			state = content.getString();

			return state;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return state;
	}

	public int getCurrentPhaseDuration(){

		Command cmd = new Command(Constants.CMD_GET_TL_VARIABLE);
		Content cnt = new Content(Constants.TL_PHASE_DURATION,id);

		cmd.setContent(cnt);

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);


		try {

			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_TL_VARIABLE, (byte)  Constants.RESPONSE_GET_TL_VARIABLE,
					(byte)  Constants.TL_PHASE_DURATION, (byte)  Constants.TYPE_INTEGER);

			return content.getInteger();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return -1;
	}
	
	public ArrayList<String> getControlledLanes(){

		Command cmd = new Command(Constants.CMD_GET_TL_VARIABLE);
		Content cnt = new Content(Constants.TL_CONTROLLED_LANES,id);
		
		cmd.setContent(cnt);
		
		//cmd.print("Command GETEDGES");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		
		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			Content content = rspMsg.validate( (byte)  Constants.CMD_GET_TL_VARIABLE, (byte)  Constants.RESPONSE_GET_TL_VARIABLE,
					 (byte)  Constants.TL_CONTROLLED_LANES, (byte)  Constants.TYPE_STRINGLIST);
			
			controlledLanes = content.getStringList();

			return controlledLanes;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setState(String newState) {		
		Command cmd = new Command(Constants.CMD_SET_TL_VARIABLE);
		Content cnt = new Content(Constants.TL_RED_YELLOW_GREEN_STATE, id,
		        Constants.TYPE_STRING);

		cnt.setString(newState);
		cmd.setContent(cnt);

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {
			SumoCom.query(reqMsg);
		} catch (IOException e) {
			System.out.println("Receiving setRouteById change Status");
			e.printStackTrace();
		}		
	}
	
	public void setProgram(String programId) {
        Command cmd = new Command(Constants.CMD_SET_TL_VARIABLE);
        Content cnt = new Content(Constants.TL_PROGRAM, id,
                Constants.TYPE_STRING);

        cnt.setString(programId);
        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);

        try {
            SumoCom.query(reqMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void setProgram(SumoTrafficLightProgram program) {
        Command cmd = new Command(Constants.CMD_SET_TL_VARIABLE);        
        Content cnt = new Content((byte)Constants.TL_COMPLETE_PROGRAM_RYG, id, Constants.TYPE_COMPOUND);
        
        List<Phase> phases = program.getPhases();
        
        ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
        ArrayList<Pair<Integer,Object>> items2 = new ArrayList<Pair<Integer,Object>>();
        items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, program.getId())); // program id
        items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, 0)); // always 0
        items.add(new Pair<Integer,Object>(Constants.TYPE_COMPOUND, items2)); // empty compound
        
        items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, 0)); // initial phase
        items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, phases.size())); // number of phases
        
        for (Phase phase: phases) {
            items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, phase.getDuration())); // duration
            items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, phase.getDuration())); // unused value
            items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, phase.getDuration())); // unused value
            items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, phase.getState())); // red/yellow/green lights
        }
        cnt.setCompound(items);

        cmd.setContent(cnt);

        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);        

        try {
            SumoCom.query(reqMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public SumoTrafficLightProgram getProgram() {
        Command cmd = new Command(Constants.CMD_GET_TL_VARIABLE);
        Content cnt = new Content(Constants.TL_COMPLETE_DEFINITION_RYG, id);
        
        cmd.setContent(cnt);
        
        RequestMessage reqMsg = new RequestMessage();
        reqMsg.addCommand(cmd);
        
        try {            
            ResponseMessage rspMsg = SumoCom.query(reqMsg);
            Content content = rspMsg.validate(
                    (byte) Constants.CMD_GET_TL_VARIABLE,
                    (byte) Constants.RESPONSE_GET_TL_VARIABLE,
                    (byte) Constants.TL_COMPLETE_DEFINITION_RYG,
                    (byte) Constants.TYPE_COMPOUND);
            
            return new SumoTrafficLightProgram(content.getCompound());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongCommand e) {
            e.printStackTrace();
        }

        return null;
	}

}
