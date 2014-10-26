import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;


public class Server {
	
	private ServerSocket server = null;
	private Vector<Socket> clients = new Vector<Socket>();
	
	Server(int port){
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendAll(String m){
		synchronized(clients){
			System.out.println("asdasdasdasd");
			Iterator<Socket> e = clients.iterator();
			System.out.println(e.hasNext());
			try {
				while(e.hasNext()){
					Socket s = e.next();
					System.out.println("Sending "+ m + " to " + s);
					DataOutputStream dis = new DataOutputStream(s.getOutputStream());
					dis.writeUTF(m);
				}
			}
		catch (IOException e1) {
				e1.printStackTrace();
		}
		}
		
	}
	 public void startListen(){
		try{
			System.out.println("Waiting for clients...");
		while(true){
			
			Socket client = server.accept();
			System.out.println("Connecting client" + client);
		
			
			clients.add(client);
			ClientThread t = new ClientThread(client, this);
			t.start();
			
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	  
	  
	}
	 
	public static void main(String[] args) {
		new Server(3000).startListen();
	}
	
	
	public class ClientThread extends Thread {
		private Server server = null;
		private Socket tclient = null;
		private DataInputStream dis;
		//private DataOutputStream dos;
		ClientThread(Socket client, Server server){
			this.tclient = client;
			this.server = server;
		}
		public void run(){
			try {
			DataInputStream dis = new DataInputStream(tclient.getInputStream());
			while(true){
				String message = dis.readUTF();
				server.sendAll(message);
			}
			}
			catch (IOException e) {
					e.printStackTrace();
			}
	
		}
		public void finalize(){
			try {
				tclient.close();
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}


}
