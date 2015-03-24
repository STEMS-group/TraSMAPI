package start;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.awt.Color;

import trasmapi.genAPI.Edge;
import trasmapi.genAPI.Polygon;
import trasmapi.sumo.SumoCom;
import trasmapi.sumo.SumoEdge;

public class InfoAgent extends Agent{

	Polygon polygon;
	Edge actionEdge;
	
	static int classID = 0;
	
	public InfoAgent(double x, double y) {
		super();

		polygon = SumoCom.newSumoPolygon(x,y, Color.BLUE);
	//	actionEdge = SumoCom.getEdge("53");
		
	//	SumoCom.subscribeContexPolygon((SumoPolygon)polygon);
	//	SumoCom.subscribeEdgeVehicles((SumoEdge)actionEdge);
	}

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {


		DFAgentDescription ad = new DFAgentDescription();
		ad.setName(getAID()); //agentID
		System.out.println("AID: "+ad.getName());

		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName()); //nome do agente    
		System.out.println("Nome: "+sd.getName());
		
		sd.setType("EdgeInformer");
		System.out.println("Tipo: "+sd.getType()+"\n\n\n");

		ad.addServices(sd); 

		try {
			DFService.register(this, ad);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		
		//addBehaviour(new InformBehaviour(this));
		
		super.setup();
	}

	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		super.takeDown();
	}
	

}
