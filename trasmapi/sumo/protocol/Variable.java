package trasmapi.sumo.protocol;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import trasmapi.sumo.vartypes.Position2D;

public class Variable {

	public byte varId;
	public byte varStatus;
	public byte varType;
	public ArrayList<Byte> varValue;

	public Variable(Buffer buf) {

		varId = buf.readByte();
		varStatus = buf.readByte();
		varType = buf.readByte();
		
		varValue = buf.readValue(varType);
		
	
	}

	public void print(String prefix) {
		System.out.println(prefix + "------ Variable ------");

		System.out.println(prefix + "varId : " + Integer.toString(varId & 0xFF, 16) + " ");
		System.out.println(prefix + "varStatus : " + Integer.toString(varStatus & 0xFF, 16) + " ");
		System.out.println(prefix + "varType : " + Integer.toString(varType & 0xFF, 16) + " ");
		
		System.out.println(prefix + "varValue : " + varValue);
		
		System.out.println("\n");

	}


	public int getInt(int pointer) {

		ByteArrayInputStream byteIn;
		DataInputStream dataIn;
		byte content[] = new byte[4];
		int result = 0;
		
		for (int i=0; i<4; i++)
		{
			content[i] = varValue.get(i+pointer);
		}
		byteIn =  new ByteArrayInputStream(content);
		dataIn = new DataInputStream(byteIn);
		try {
			result = dataIn.readInt();
			dataIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;

	}

	public ArrayList<String> getStringList() {

		//read string array
		int numStrings = getInt(0);

		int pointer = 4;
		
		ArrayList<String> stringList = new ArrayList<String>();
		
		for(int i=0 ; i<numStrings ; i++){

			int strLength = getInt(pointer);

			String str = new String();
			byte[] value = new byte[strLength];

			pointer += 4;
			str = "";
			
			for(int j = 0; j < strLength; j++)
				value[j] = varValue.get(j+pointer);
			
			for(int j = 0; j < strLength; j++)
				str += (char) value[j];
			
			pointer += strLength;
			
			stringList.add(str);
		}
		
		return stringList;
	}

	public double getDouble(int pointer) {

		byte content[] = new byte[8];
		double result = 0.0;
		
		for (int i=0; i<8; i++)
			content[i] = varValue.get(i+pointer);

		ByteArrayInputStream byteIn;
		DataInputStream dataIn;
		
		byteIn =  new ByteArrayInputStream(content);
		dataIn = new DataInputStream(byteIn);
		try {
			result = dataIn.readDouble();
			dataIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public Position2D getPosition() {

		Position2D pos = new Position2D();
		pos.x = getDouble(0);
		pos.y = getDouble(8);
		return null;
	}

	public String getString() {

		int length = getInt(0);

		String str = new String();

		str = "";
		
		byte[] value = new byte[length];
		
		for(int i = 0; i< length;i++)
			value[i] = varValue.get(i+4);
		
		for(int j=0; j<length; j++)
			str += (char) value[j];
		
		return str;
	}

	public Color getColor() {

		return new Color((varValue.get(0) & 0xFF),(varValue.get(1) & 0xFF),(varValue.get(2) & 0xFF),(varValue.get(3) & 0xFF));
		 
	}

}
