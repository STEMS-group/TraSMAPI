package trasmapi.sumo;

import java.io.IOException;
import java.util.ArrayList;

import trasmapi.genAPI.Edge;
import trasmapi.genAPI.Route;
import trasmapi.genAPI.exceptions.WrongCommand;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;
import trasmapi.sumo.protocol.ResponseMessage;

public class SumoRoute extends Route{

	public ArrayList<SumoEdge> edges;

	public SumoRoute() {
		super();
	}
	
	public SumoRoute(String routeId) {
		super(routeId);
		
		edges = new ArrayList<SumoEdge>();
		
	}


	public ArrayList<SumoEdge> getEdges() {
		

		Command cmd = new Command(Constants.CMD_GET_ROUTE_VARIABLE);
		Content cnt = new Content(Constants.VAR_EDGES, id);
		
		cmd.setContent(cnt);
		
		cmd.print("Command getEdges");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			ResponseMessage rspMsg = SumoCom.query(reqMsg);
			
			Content content = rspMsg.validate((byte) Constants.CMD_GET_ROUTE_VARIABLE,  (byte)  Constants.RESPONSE_GET_ROUTE_VARIABLE,
					 (byte)  Constants.VAR_EDGES, (byte)  Constants.TYPE_STRINGLIST);
			
			ArrayList<String> edgeList = content.getStringList();

			System.out.println("EdgeList : "+edgeList);
			
			for(String e:edgeList)
				edges.add(new SumoEdge(e));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongCommand e) {
			e.printStackTrace();
		}
		return null;
	}



	public void setEdges(ArrayList<String> newStringList){

		for(String s: newStringList)
			edges.add(new SumoEdge(s));
		
	}

	public ArrayList<String> getStringList(){
		
		ArrayList<String> strings = new ArrayList<String>();
		
		for(Edge e: edges)
			strings.add(e.getId());
		
		return strings;
		
	}
	
	public void addRoute() {
		
		Command cmd = new Command(Constants.CMD_SET_ROUTE_VARIABLE);
		
		Content cnt = new Content(Constants.ADD,id,Constants.TYPE_STRINGLIST);
		
		cnt.setStringList(getStringList());
		
		cmd.setContent(cnt);
		
		//cmd.print("addRoute");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);
		

		try {
			
			SumoCom.query(reqMsg);
			
			
		} catch (IOException e) {
			System.out.println("Receiving addRoute change Status");
			e.printStackTrace();
		}		
	}	
}
