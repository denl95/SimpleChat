import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class ChatClient extends Application implements Runnable {
	private Socket socket;
	private TextArea chat;
	private TextArea message;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	String host = "localhost";
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage arg0)  {
		
		try {
			socket = new Socket(InetAddress.getByName(host), 3000);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		chat = new TextArea();
		chat.setEditable(false);
		//chat.setDisable(true);;
		message = new TextArea();
		message.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent arg0) {
					if(arg0.getCode() == KeyCode.ENTER){
						try {
							dos = new DataOutputStream(socket.getOutputStream());
							dos.writeUTF(message.getText());
						} catch (IOException e) {
							e.printStackTrace();
						}
						message.setText("");
					}
				}
		}
		);
		
		BorderPane root = new BorderPane();
		root.setBottom(message);
		root.setCenter(chat);
		
		Scene scene = new Scene(root, 640, 480);
		
		arg0.setTitle("Hello World");
		arg0.setScene(scene);
		arg0.show();
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
		
	}
	
	@Override
	public void run() {
		
		try {
			while(true){
				
					String str = dis.readUTF();
					System.out.println("Response"+ str);
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							chat.appendText(str + "\n");
						}
					}
					);
			}
		}
		catch(IOException e){e.printStackTrace();}
		
	}
	public void finalize(){
		try {
			dis.close();
			dos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
