package trasmapi.sumo.protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Buffer {
	ArrayList<Byte> bytes;
	int position;
	
	public Buffer(byte[] packet)
	{
		if (packet == null)
			throw new NullPointerException("byte[] can't be null");

		bytes = new ArrayList<Byte>();
		position = 0;

		for (int i=0; i < packet.length; i++){
			writeByte(packet[i]);
		}
	}
	
	public void writeByte(int value) throws IllegalArgumentException
	{
		if (value < -128 || value > 127)
			throw new IllegalArgumentException("Error writing byte: byte value may only range from -128 to 127.");

		bytes.add(new Byte( (byte)(value) ));
	}
	
	/**
	 * Read a byte value from the List
	 * @return the read byte as an Integer value (unsigned)
	 */
	public byte readByte() throws IllegalStateException
	{
		Byte b = bytes.get(position);
		position++;
		
		return b;
		
	}

	public void print() {

		System.out.print("\t");
		System.out.print("Buff size: "+ bytes.size());
		System.out.println();
		for(Byte b : bytes)
			System.out.print(Integer.toString( b & 0xFF, 16) + " ");
		System.out.println();
		

	}

	public int readInt() {
		
		ByteArrayInputStream byteIn;
		DataInputStream dataIn;
		byte content[] = new byte[4];
		int result = 0;
		
		for (int i=0; i<4; i++)
		{
			content[i] = (byte) readByte();
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

	public String readString() {

		byte content[];
		String result = new String("");
		int length;
		
		length = readInt();
		content = new byte[length];
		for (int i=0; i<length; i++)
		{
			content[i] = (byte) readByte();
		}
	
		try {
			
			result = new String(content, "US-ASCII");
	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public boolean end() {
		return position >= bytes.size()-1;
	}

	public ArrayList<Byte> readValue(byte varType) {

		ArrayList<Byte> values = new ArrayList<Byte>();
		//TODO  AQUI
		switch((byte)varType){
		case Constants.TYPE_BYTE:
			values = readTypeByte();
			break;
		case Constants.TYPE_INTEGER:
			values = readTypeInteger();
			break;
		case Constants.TYPE_STRING:
			values = readTypeString();
			break;
		case Constants.TYPE_DOUBLE:
			values = readTypeDouble();
			break;
		case Constants.TYPE_STRINGLIST:
			values = readTypeStringList();
			break;
		case Constants.POSITION_2D:
			for(int i=0; i< 16;i++)
				values.add(readByte());
			break;
		case Constants.TYPE_COLOR:
			for(int i=0; i< 4;i++)
				values.add(readByte());
			break;
		case Constants.TYPE_COMPOUND:
		    values = readTypeCompound();
		    break;
		default:
			System.out.println("PROBLEM! VAR TYPE UNKNOWN! : " + Integer.toString( varType & 0xFF, 16) + " ");
			break;
		}
		
		return values;
	}

	private ArrayList<Byte> readTypeStringList() {
		ArrayList<Byte> value = new ArrayList<Byte>();
		try {

			//copy length
			byte[] array = new byte[4];
			for(int i=0;i<4;i++)
				array[i] = readByte();

			ByteArrayInputStream byteIn =  new ByteArrayInputStream(array);
			DataInputStream dataIn = new DataInputStream(byteIn);
			
			int numStrings = 0;
			numStrings = dataIn.readInt();

			dataIn.close();

			for(int i=0;i<4;i++)
				value.add(array[i]);
			
			for(int i=0;i<numStrings;i++){
				ArrayList<Byte> temp = readTypeString();
				
				for(Byte b : temp)
					value.add(b);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	private ArrayList<Byte> readTypeCompound() {
        ArrayList<Byte> value = new ArrayList<Byte>();
        
        byte[] array = new byte[4];
        for(int i = 0; i < 4; i++) {
            array[i] = readByte();
            value.add(array[i]);
        }

        ByteArrayInputStream byteIn =  new ByteArrayInputStream(array);
        DataInputStream dataIn = new DataInputStream(byteIn);
        
        int numItems = 0;
        try {
            numItems = dataIn.readInt();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for(int i = 0; i < numItems; i++) {
            byte type = readByte();
            value.add(type);
            
            switch (type) {            
            case Constants.TYPE_DOUBLE:
                for (Byte b : readTypeDouble()) {
                    value.add(b);
                }
                break;
            case Constants.TYPE_STRING:
                for (Byte b : readTypeString()) {
                    value.add(b);
                }
                break;
            case Constants.TYPE_INTEGER:
                for (Byte b : readTypeInteger()) {
                    value.add(b);
                }
                break;
            case Constants.TYPE_BYTE:
                for (Byte b : readTypeByte()) {
                    value.add(b);
                }
                break;
            case Constants.TYPE_UBYTE:
                // TODO:
                break;
            case Constants.TYPE_COLOR:
                // TODO:
                break;
            case Constants.TYPE_POLYGON:
                // TODO:
                break;
            case Constants.TYPE_COMPOUND:
                for (Byte b : readTypeCompound()) {
                    value.add(b);
                }
                break;
            }
        }
        
        return value;
	}
	
	private ArrayList<Byte> readTypeDouble() {
		ArrayList<Byte> value = new ArrayList<Byte>();
		for(int i=0; i< 8;i++)
			value.add(readByte());
		return value;
	}

	private ArrayList<Byte> readTypeString() {
		ArrayList<Byte> value = new ArrayList<Byte>();
		
		try {

			//copy length
			byte[] length = new byte[4];
			for(int i=0;i<4;i++)
				length[i] = readByte();

			ByteArrayInputStream byteIn =  new ByteArrayInputStream(length);
			DataInputStream dataIn = new DataInputStream(byteIn);
			int size = 0;
			size = dataIn.readInt();
			dataIn.close();

			for(int i=0;i<4;i++)
				value.add(length[i]);
		
			for(int i=0;i<size;i++)
				value.add(readByte());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;
	}

	private ArrayList<Byte> readTypeInteger() {
		ArrayList<Byte> value = new ArrayList<Byte>();
		for(int i = 0; i<4;i++)
			value.add(readByte());
		return value;
	}

	private ArrayList<Byte> readTypeByte() {
		ArrayList<Byte> value = new ArrayList<Byte>();
		value.add(readByte());
		return value;
	}
}
