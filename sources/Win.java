package com.ilnur.WindowClient;
import com.ilnur.admin.question.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.text.*;
public  class Win{
	JTextField message;
	JTextField name;
	JButton send;
	Socket sock;
	ObjectOutputStream writer;
	ObjectInputStream reader;
	JTextArea textChat;
	JButton setName;
	String myname;
	JTextArea nameofname;
	JButton setIpPort;
	JTextField portText;
	JTextField ipText;
	JPanel ipPort;
	String ip;
	int port;
	JButton[] pKeys;
	JPanel question;
	JTextArea quest;
	Vector<String> listVector=new Vector<String>();
	boolean isAnswer;
	String myLANIP;
	public static void main(String[] args) {
		Win chat = new Win();
		chat.build();
	}
	public void build(){
		try{

			InetAddress addr = InetAddress.getLocalHost();         
			myLANIP = addr.getHostAddress();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		myname = myLANIP;
		JFrame GUI = new JFrame("Участник.");

		Image img = new ImageIcon("images/client.png").getImage();
		GUI.setIconImage(img);
		message = new JTextField(20);
		name= new JTextField(5);
		setName = new JButton("OK!");
		setName.addActionListener(new name());
		setName.setEnabled(false);
		name.setEditable(false);
		try {
			InetAddress IP=InetAddress.getLocalHost();
			name.setText(IP.getHostAddress());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		question = new JPanel();
		quest = new JTextArea("Здесь будут вопросы администратора.");
		ipPort= new JPanel();
		ipPort.setBackground(new Color(140,140,140));
		portText = new JTextField(4);
		ipText = new JTextField(10);
		ipText.setText(myLANIP);
		setIpPort = new JButton("Подключиться.");
		setIpPort.addActionListener(new Connecting());
		nameofname = new JTextArea("Имя: ",1,15);
		message.setText("");
		message.setEditable(false);
		message.addActionListener(new SendIt());
		JPanel panel = new JPanel();	
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panelOuter = new JPanel();
		Color blacker=new Color(100,100,100);
		Color whiter=new Color(100,100,100);
		panel.setBackground(blacker);
		panel3.setBackground(blacker); 
		question.add(quest);

		

		nameofname.setEditable(false);
		nameofname.setBackground(Color.red);
		textChat = new JTextArea(10,20);
		textChat.setBackground(new Color(200,200,200));
		textChat.setEditable(false);
		DefaultCaret caret = (DefaultCaret)textChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroller = new JScrollPane(textChat);

		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		ipPort.add(ipText);
		ipPort.add(portText);
		ipPort.add(setIpPort);
		panel.add(nameofname);
		panel.add(name);
		panel.add(setName);
		panel2.add(scroller);
		panel3.add(message);
		panelOuter.add(panel);
		panelOuter.add(panel);
		panelOuter.add(question);
		panelOuter.add(panel2);
		panelOuter.add(panel3);

		panelOuter.setLayout(new BoxLayout(panelOuter, BoxLayout.Y_AXIS));

		GUI.getContentPane().add(BorderLayout.CENTER, panelOuter);
		GUI.getContentPane().add(BorderLayout.NORTH, ipPort);
		GUI.setSize(400,320);
		GUI.setVisible(true);



	}
	private void runNet(String Ip,int Port){
		try{
			sock = new Socket(Ip,Port);
			ipText.setBackground(Color.white);
			portText.setBackground(Color.white);
			writer = new ObjectOutputStream(sock.getOutputStream());
			reader= new ObjectInputStream(sock.getInputStream());
			System.out.print("Connection ok.");
			setName.setEnabled(true);
			name.setEditable(true);
			setIpPort.setEnabled(false);
			ipText.setEditable(false);
			portText.setEditable(false);
			name.requestFocus();

		}
		catch(IOException ex){
			ex.printStackTrace();
			ipText.setBackground(Color.red);
			portText.setBackground(Color.red);
		}
	}
	public class name  implements ActionListener {
		public void actionPerformed(ActionEvent ev){

			myname=name.getText();
			name.setEditable(false);
			setName.setEnabled(false);
			message.setEditable(true);
			nameofname.setBackground(Color.white);
			try{

				writer.writeObject(myname+" вошел в систему.");	
				writer.writeObject("");
				
			}
			catch (IOException e) {
				System.out.println("Error: 128 Win");
			}
			message.requestFocus();		
		}
	}
	class SendIt implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try{

				if (!message.getText().equals("")) {
					writer.writeObject(myname+": "+message.getText());
					// System.out.println(myname+": "+message.getText());				
					
					nameofname.setEditable(false);
					nameofname.setBackground(Color.white);
				}
				
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			message.setText("");
			message.requestFocus();			
		}
	}

	public class IncomingReader implements Runnable {

		public void run(){
			Object mess2=null;
			String t=" ";
			int te=5;
			try{
				System.out.println("Ok!");
				while((mess2=reader.readObject())!=null){
					if(((String)mess2.getClass().getName()).equals("com.ilnur.admin.question.Question")){
						writeIt((Question)mess2);
					}
					else{
						writeIt((String)mess2);
					}
				}


			}


			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void doQuery(String query){
		try{
			if (query.equals("exit")) {
				message.setEditable(false);
				send.setEnabled(false);
				writer.writeObject("");
				writer.writeObject(myname+" quit");

			}
			else  {
				writer.writeObject( "Error.");
			}

		}
		catch (IOException e) {
			System.out.print("Error:188");
		}
	}
	class Connecting  implements ActionListener {
		public void actionPerformed(ActionEvent ev){


			port = Integer.parseInt(portText.getText());
			ip = ipText.getText();
			// if (!(ipText.getText()).equals("")&&!(portText.getText()).equals("")) {
			runNet(ip,port);
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();


		}
	}
	public void writeIt(Question t){
		quest.setText(t.question);
	}
	public void writeIt(String t){
		textChat.append(t+"\n");
	}

}