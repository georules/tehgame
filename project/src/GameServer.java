import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class GameServer {
	static Object buffer;
	public static void main(String args[]) throws Exception {
		System.out.println("Server running");
		
		ServerSocket servsock = new ServerSocket(1337);
		
		System.out.println("Waiting for client 1");
		Socket sock1 = servsock.accept();
		System.out.println("Accepted connection: " + sock1);

		System.out.println("Waiting for client 2");
		Socket sock2 = servsock.accept();
		System.out.println("Accepted connection: " + sock2);
		
		ArrayList<Socket> sockets = new ArrayList<Socket>();
		sockets.add(sock1);
		sockets.add(sock2);

		ArrayList<ObjectOutputStream> outputs = new ArrayList<ObjectOutputStream>();
		ArrayList<ObjectInputStream> inputs = new ArrayList<ObjectInputStream>();
		int si = 1;
		for (Socket s : sockets) {
			ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
			outputs.add(os);
			sendhi(os,si++);
		}
		for (Socket s: sockets) {
			ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			inputs.add(is);
			System.out.println(is.readInt());
		}
		
		System.out.println("Transferring data.");
		boolean sOpen = true;
		long x = 0;
		while(sOpen) {
			for (int i = 0; i < sockets.size(); i++) {
				for (int j = 0; j < sockets.size(); j++) {
					if (i!=j) {
						System.out.println("Downloading data." + i);
						download(inputs.get(i));
						System.out.println("Transferring data." + j);
						transfer(outputs.get(j));
					}
				}
				sOpen = sockets.get(i).isConnected();
			}
		}
	}
	private static void sendhi(ObjectOutputStream os, int i) {
		try {
			os.writeInt(i);
			os.flush();
			System.out.println("Sending hi: " + i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("could not send hi");
		}
	}
	public static void download(ObjectInputStream i) {
		try{
			buffer = i.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void transfer(ObjectOutputStream o) throws Exception{
		try{
			o.writeObject(buffer);
			o.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
