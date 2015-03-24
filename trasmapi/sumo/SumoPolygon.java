package trasmapi.sumo;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import start.DriverAgent;
import trasmapi.genAPI.Polygon;
import trasmapi.sumo.protocol.Command;
import trasmapi.sumo.protocol.Constants;
import trasmapi.sumo.protocol.Content;
import trasmapi.sumo.protocol.RequestMessage;

public class SumoPolygon extends Polygon {

	public static int classId = 1;
	public String id;
	public Color color;
	public boolean filled = true;
	public int layer = 6;
	public ArrayList<Pair<Double,Double>> shape;
	public double sensingRadius = 150;
	
	//radius
	private double drawRadius = 6.0;
	private Color radiusColor = Color.cyan;
	

	public SumoPolygon() {

	}

	public SumoPolygon(double x, double y, Color colorP) {

		shape = new ArrayList<Pair<Double,Double>>();
		
		id = ""+ classId++;
		color = colorP;
		
		sensingRadius = 125 + DriverAgent.rand.nextInt(50);

		shape.add(new Pair<Double,Double>(x,y+drawRadius));
		shape.add(new Pair<Double,Double>(x-drawRadius*Math.cos(Math.PI/6.0),y+drawRadius*Math.sin(Math.PI/6.0)));
		shape.add(new Pair<Double,Double>(x-drawRadius*Math.cos(Math.PI/6.0),y-drawRadius*Math.sin(Math.PI/6.0)));
		shape.add(new Pair<Double,Double>(x,y-drawRadius));
		shape.add(new Pair<Double,Double>(x+drawRadius*Math.cos(Math.PI/6.0),y-drawRadius*Math.sin(Math.PI/6.0)));
		shape.add(new Pair<Double,Double>(x+drawRadius*Math.cos(Math.PI/6.0),y+drawRadius*Math.sin(Math.PI/6.0)));
		
		add();
		
		ArrayList<Pair<Double,Double>> radiusShape = new ArrayList<Pair<Double,Double>>();

		for(int i=0; i<363; i+=2)
			radiusShape.add(new Pair<Double,Double>(x+sensingRadius*Math.cos(i*Math.PI*2.0/360.0),y+sensingRadius*Math.sin(i*Math.PI*2.0/360.0)));
		
		addRadius(radiusShape);
		
	}

	private void addRadius(ArrayList<Pair<Double, Double>> radiusShape) {

		Command cmd = new Command(Constants.CMD_SET_POLYGON_VARIABLE);
		
		Content cnt = new Content((byte)Constants.ADD,id+"_r",Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, id+"_r"));
		items.add(new Pair<Integer,Object>(Constants.TYPE_COLOR, radiusColor));
		items.add(new Pair<Integer,Object>(Constants.TYPE_UBYTE, 0));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, layer));
		items.add(new Pair<Integer,Object>(Constants.TYPE_POLYGON, radiusShape));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

	//	cmd.print("AddPolygonRadius");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {
			
			SumoCom.query(reqMsg);
			
			
		} catch (IOException e) {
			System.out.println("Receiving AddPolygon change Status");
			e.printStackTrace();
		}
		
		
	}

	private void add() {

		Command cmd = new Command(Constants.CMD_SET_POLYGON_VARIABLE);
		
		Content cnt = new Content((byte)Constants.ADD,id,Constants.TYPE_COMPOUND);
		
		ArrayList<Pair<Integer,Object>> items = new ArrayList<Pair<Integer,Object>>();
		
		items.add(new Pair<Integer,Object>(Constants.TYPE_STRING, id));
		items.add(new Pair<Integer,Object>(Constants.TYPE_COLOR, color));
		items.add(new Pair<Integer,Object>(Constants.TYPE_UBYTE, (filled == true?1:0)));
		items.add(new Pair<Integer,Object>(Constants.TYPE_INTEGER, layer));
		items.add(new Pair<Integer,Object>(Constants.TYPE_POLYGON, shape));
		
		cnt.setCompound(items);
		
		cmd.setContent(cnt);

	//	cmd.print("AddPolygon");

		RequestMessage reqMsg = new RequestMessage();
		reqMsg.addCommand(cmd);

		try {
			
			SumoCom.query(reqMsg);
			
			
		} catch (IOException e) {
			System.out.println("Receiving AddPolygon change Status");
			e.printStackTrace();
		}
		
		
	}
}
