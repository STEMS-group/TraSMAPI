package trasmapi.sumo.protocol;

import java.util.ArrayList;

public class SubscriptionResponse extends Content {

	public int subscriptionId;
	public String objectId;
	public ArrayList<Variable> variables;
	
	public SubscriptionResponse(Buffer buf, int id) {
		
		subscriptionId = id;
		
		objectId = buf.readString();
		
		int numVariables = buf.readByte();
		
		variables = new ArrayList<Variable>();
		for(int i=0; i<numVariables;i++)
			variables.add(new Variable(buf));
		
	}

	public void print(String prefix) {

		System.out.println(prefix + " 创创 Content 创创");

		System.out.println(prefix + "subscriptionId : "+Integer.toString( subscriptionId & 0xFF, 16));
		System.out.println(prefix + "objectId : "+objectId);
		System.out.println(prefix + "numVariables : "+ variables.size());
		
		for(Variable v:variables)
			v.print("\t\t\t");

	}
}
