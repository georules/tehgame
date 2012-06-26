import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;

public class Network implements Runnable{
	boolean doit = true;
	boolean startup = true;
	int prevWorld;
	Socket sock;
	ObjectInputStream input;
	ObjectOutputStream output;
	Network()	{
	}
	
	public void send() {
		System.out.println("Send function");
		if (prevWorld != World.things.size()) {
			System.out.println("Sending update");
			try {
				output.writeObject(World.things);
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			prevWorld = World.things.size();
		}
		else{
			System.out.println("no update");
		}
		
	}
	
	public void get() {
		
		try {
			CopyOnWriteArrayList<Thing> in = (CopyOnWriteArrayList<Thing>)input.readObject();
			ArrayList<String> hashes = new ArrayList<String>();
			for (Thing t : World.things) {
				hashes.add(t.hash);
			}
			for (Thing t : in) {
				if(!hashes.contains(t.hash)) {
					System.out.println("Update Time!");
					World.things.add(new Thing(t));
				}
			}
			in = null;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	public void run() {
		prevWorld = 0;
		int order = 0;
		try {
			sock = new Socket("localhost",1337);
			System.out.println("net1");
			output = new ObjectOutputStream(new BufferedOutputStream(sock.getOutputStream()));
			System.out.println("net2");
			input = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));
			System.out.println("getting int");
			order = input.readInt();
			System.out.println("net3 " + order);
			
			output.writeInt(order + 5);
			output.flush();
			
		}
		catch(Exception e) {
			
		}
		while(doit) {
			//System.out.println("net " + order);
			if (order == 1) {
				send();
				get();
			}
			if(order == 2) {
				get();
				send();
			}
		}
	}
	
	public void stop() {
		doit=false;
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void finalize() throws Throwable{
		stop();

		super.finalize();
	}
}
