package com.ilnur.mainAll;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.text.*;
import com.ilnur.admin.WindowAdmin.*;
import com.ilnur.admin.question.*;
import com.ilnur.WindowClient.*;
import com.ilnur.server.*;
import com.ilnur.winserver.*;
public class MainWindow  {
	JButton bAdmin;
	JButton bClient;
	JButton bServer;
	JFrame mainWin;
	public static void main(String[] args) {
		MainWindow startWork = new MainWindow();
		startWork.build();

	}
	void build(){
		mainWin = new JFrame("MIR_Test");
		Image img = new ImageIcon("images/main.png").getImage();
		mainWin.setIconImage(img);
		JPanel pServer = new JPanel();
		JPanel pAdmin = new JPanel();
		JPanel pClient = new JPanel();
		bAdmin = new JButton("Запустить адмиистратора.");
		bClient = new JButton("Запустить клиента.");
		bServer = new JButton("Запустить сервер.");
		bServer.addActionListener(new server());
		bClient.addActionListener(new client());
		bAdmin.addActionListener(new admin());
		pClient.add(bClient);
		pAdmin.add(bAdmin);
		pServer.add(bServer);
		JPanel outPanel = new JPanel();
		outPanel.add(pClient);
		outPanel.add(pAdmin);
		outPanel.add(pServer);
		outPanel.setLayout(new BoxLayout(outPanel, BoxLayout.Y_AXIS));
		mainWin.getContentPane().add(BorderLayout.CENTER, outPanel);
		mainWin.setSize(250,150);
		mainWin.setVisible(true);

	}
	class server implements ActionListener{
		public void actionPerformed(ActionEvent event){
			WinOfServer GUIServ = new WinOfServer();
			GUIServ.build();
			bServer.setEnabled(false);
			(new MainWindow()).build();
			mainWin.setVisible(false);
		}
	}	
	class admin implements ActionListener{
		public void actionPerformed(ActionEvent event){
			Admin administrator = new Admin();
			administrator.buildWork("Я");
			// mainWin.setVisible(false);
			bServer.setEnabled(false);
			bAdmin.setEnabled(false);
		}
	}	
	class client implements ActionListener{
		public void actionPerformed(ActionEvent event){
			Win chat = new Win();
			chat.build();
			// mainWin.setVisible(false);
			bServer.setEnabled(false);

		}
	}
}