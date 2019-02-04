package com.ilnur.winserver;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import com.ilnur.server.*;
public class WinOfServer {
	public startServer serverS;
	JTextField port; 
	String myLANIP;
	Thread letStart;
	public static void main(String[] args) {

		WinOfServer GUIServ = new WinOfServer();
		GUIServ.build();
	}
	public void build(){
		serverS= new startServer();
		JFrame Win = new JFrame("Сервер.");

		Image img = new ImageIcon("images/server.png").getImage();
		Win.setIconImage(img);
		JPanel pan = new JPanel();
		try{

			InetAddress addr = InetAddress.getLocalHost();         
			myLANIP = addr.getHostAddress();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		JTextArea IP = new JTextArea(myLANIP);
		port = new JTextField("5000");
		JButton startS = new JButton("Ok!");
		startS.addActionListener(new StartServer());
		IP.setEnabled(false);
		pan.add(IP);
		pan.add(port);
		pan.add(startS);
		Win.getContentPane().add(BorderLayout.CENTER, pan);
		Win.setSize(250,111);
		Win.setVisible(true);
	}
	public class letStart implements Runnable {
		public void run(){
			
			serverS.go(Integer.parseInt(port.getText()));
		}
	}
	public class StartServer implements ActionListener {
		public void actionPerformed(ActionEvent ev){
			new Thread(new letStart()).run();
		}
	}
}