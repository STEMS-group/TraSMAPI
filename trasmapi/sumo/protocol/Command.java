
package trasmapi.sumo.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import trasmapi.genAPI.exceptions.WrongCommand;

public class Command {

	private boolean debug = false;
	
	public static final int HEADER_SIZE = 
			Byte.SIZE/8     // length 0
		  + Integer.SIZE/8  // integer length
		  + Byte.SIZE/8;     // command id
	
	public int length;
	public int cmdId;
	public Content content;
	public ArrayList<Byte> cmdHeader = new ArrayList<Byte>();;
	
	public Command(){
		
	}

	public Command(int cmd) {
		if (cmd > 255)
			throw new IllegalArgumentException("id should fit in a byte");
		content = new Content();
		cmdHeader = new ArrayList<Byte>();
		this.cmdId = cmd;
	}
	
	public Command(byte[] command) {
		
		length = (int) command[0];
		if(debug) System.out.println("Length : " + length);
		if(length == 0)
		{
			length = command[1] << 24 | command[2] << 16 | command[3] << 8 | command[4];
			
			cmdId = command[5];
			
			byte[] cont = new byte[length-6];
			
			for(int i= 6; i< length;i++)
				cont[i-6] = command[i];
			
			content = new Content(cont);
		}
		else
		{
			cmdId = command[1];

			byte[] cont = new byte[length-2];
			
			for(int i= 2; i< length;i++)
				cont[i-2] = command[i];
			
			content = new Content(cont);
			
		}
	}

	public Command(Buffer buf) {

		if(debug) System.out.println("------- Command --------");
		
		length = buf.readByte();
		if(debug) System.out.println("Command 1stLength " + length);

		if(length == 0){
			length = buf.readInt();
			if(debug) System.out.println("Command 2ndLength " + length);
		}

		cmdId = buf.readByte();
		if(debug) System.out.println("cmdResponse : " + Integer.toString(cmdId & 0xFF, 16) + " ");

		if(cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_EDGE_VARIABLE ||
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_GUI_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_INDUCTIONLOOP_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_JUNCTION_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_LANE_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_MULTI_ENTRY_EXIT_DETECTOR_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_POI_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_POLYGON_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_ROUTE_VARIABLE || 
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_SIM_VARIABLE ||  
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_TL_VARIABLE ||  
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_VEHICLE_VARIABLE ||  
				cmdId == (byte) Constants.RESPONSE_SUBSCRIBE_VEHICLETYPE_VARIABLE){

			content = new SubscriptionResponse(buf,cmdId);
		}
		else{
			
			content = new Content(buf);
			
		}

		if(debug) System.out.println("\n");
	}

	public void setContent(Content cnt) {

		content = cnt;

		buildHeader();

	}
	
	private void buildHeader() {

		writeByte(0);
		length = HEADER_SIZE+content.length();
		writeInt(length);
		writeByte(cmdId);
		
	}

	


	private void writeInt(int value) {

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream(4);
		DataOutputStream dataOut = new DataOutputStream(byteOut);
		
		byte bytes[] = new byte[4]; 
		
		try {
			dataOut.writeInt(value);
			dataOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bytes = byteOut.toByteArray();
		
		for (int i=0; i<4; i++)
			writeByte(bytes[i]);
		
	}

	private void writeByte(int value) {
		
		byte b = (byte) value;
		if (b < -128 || b > 127)
			throw new IllegalArgumentException("only range from -128 to 127.");

		cmdHeader.add(b);
	}

	public void print(String prefix) {
		
		System.out.print(prefix);
		for(Byte b : cmdHeader)
			System.out.print(Integer.toString(b & 0xFF, 16) + " ");
		System.out.println();


		System.out.println(prefix + "cmdId : "+Integer.toString(cmdId & 0xFF, 16));
		content.print("\t\t");

		
	}

	public int length() {
		return length;
	}

	public void sendCommand(DataOutputStream out) throws IOException {

		for(Byte b: cmdHeader)
			out.writeByte(b);
		
		content.sendContent(out);
	}

	public Content validate(int responseGetVehicleVariable, int idList, int typeStringlist) throws WrongCommand {

		if(responseGetVehicleVariable != cmdId)
			throw new WrongCommand("RESPONSE check: Was expecting " + Integer.toString(responseGetVehicleVariable & 0xFF, 16) +
									" and got " +  Integer.toString(cmdId & 0xFF, 16));
		
		content.validate(idList, typeStringlist);
		
		return content;
		
	}
	
}
