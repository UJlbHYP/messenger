package com.ilnur.admin.WindowAdmin;
import com.ilnur.admin.question.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.*;
import javax.swing.text.*;
public class  Admin {
	String testtt;
	String in;
	int sPort;
	String sIp;
	JTextField IP;
	JTextField PORT;
	JFrame AdWin;
	JTextArea name;
	JTextField login;
	JPasswordField pass;
	char[] sPass;
	String sLogin;
	JButton submitter;	
	Socket sock;
	ObjectOutputStream writer;
	JTextArea textChat;
	ObjectInputStream reader;
	JButton connect;
	JPanel formAdmin;
	JButton send;
	JButton askQ;
	JTextField questionA;
	JTextField answerA;
	JFileChooser answ_base;
	JButton upload;
	Vector<String> listVector=new Vector<String>();
	boolean v;
	boolean dos;
	int vir;
	String myLANIP;
	int tempInt = 5;
	public static void main(String[] args) {
		(new Admin()).buildWork("Я");
	}

	public void buildWork(String userName){
		v=false;
		JFrame work = new JFrame("Администратор.");

		Image img = new ImageIcon("images/admin.png").getImage();
		work.setIconImage(img);
		work.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		IP= new JTextField(15);
		try{

			InetAddress addr = InetAddress.getLocalHost();         
			myLANIP = addr.getHostAddress();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		IP.setText(myLANIP);
		PORT = new JTextField(4);
		PORT.setText("5000");
		PORT.addActionListener(new ConnectToServer()); 
		textChat = new JTextArea(10,30);
		textChat.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)textChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		answ_base=new JFileChooser();
		JPanel outBan = new JPanel();
		outBan.setLayout(new BoxLayout(outBan, BoxLayout.Y_AXIS));
		JPanel connInfo = new JPanel();
		textChat.setSize(300,300);
		textChat.setBackground(new Color(200,200,200));
		JScrollPane scroller = new JScrollPane(textChat);
		textChat.setEditable(false);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setSize(200,150);
		connInfo.add(IP);
		connInfo.add(PORT);
		formAdmin = new JPanel();
		JPanel chat = new JPanel();
		questionA = new JTextField(18);
		answerA = new JTextField(8);
		askQ=new JButton("Задать вопрос.");
		askQ.addActionListener(new Ask());
		chat.add(scroller);

		JPanel sender = new JPanel();
		sender.add(questionA);
		sender.add(answerA);
		sender.add(askQ);


		outBan.add(connInfo);
		outBan.add(chat);
		outBan.add(sender);
		work.getContentPane().add(BorderLayout.CENTER, outBan);
		work.setSize(400,500);
		work.setVisible(true);
	}
	class SubmitData implements ActionListener {
		public void actionPerformed(ActionEvent e){
			sLogin = login.getText();
			sPass =new char[18];
			sPass = pass.getPassword();
			System.out.print(sLogin+" "+sPass);

			AdWin.setVisible(false);
			(new Admin()).buildWork(sLogin);
		}
		
	}
	class ConnectToServer implements ActionListener {
		public void actionPerformed(ActionEvent e){
			sPort = Integer.parseInt(PORT.getText());
			sIp = IP.getText();
			
			runNet(sIp,sPort);
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();
			// name.setEditable(true);
			PORT.setEnabled(false);
			IP.setEditable(false);
		}
	}
	private void runNet(String Ip,int Port){
		try{
			sock = new Socket(Ip,Port);
			writer = new ObjectOutputStream(sock.getOutputStream());
			reader= new ObjectInputStream(sock.getInputStream());
			System.out.print("Connection ok.");


		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	public class IncomingReader implements Runnable {

		public void run(){
			tempInt=5;
			Object mess2=null;
			String t=" ";
			dos=false;
			vir=0;
			try{
				System.out.println("Ok!");
				while((mess2=reader.readObject())!=null){
					if (!((String)mess2.getClass().getName()).equals("com.ilnur.admin.question.Question")) {
						
						testtt = (String) mess2;
						textChat.append(testtt+"\n");
						System.out.println(testtt+" - "+in+"\n");
						if (in!=null) {
							
							if (CheckAnswer((String)mess2,answerA.getText())&&v) {
								writer.writeObject("Правильно! Ответ: "+answerA.getText()+"\nОтветил правильно: "+((String)mess2).split(": ")[0]+"!" );		
								writer.writeObject("");
								questionA.setText("");
								questionA.setEditable(true);
								answerA.setText("");		
								answerA.setEditable(true);
								askQ.setEnabled(true);
								dos=false;
								v=false;
								in=null;
							}
						}

					}
				}

			}

			catch (Exception e) {
				e.printStackTrace();
			}

			
		}
		
	}
	static boolean CheckAnswer(String orig,String ex){
		String pat = "^.+\\:\\s"+Pattern.quote(ex)+"[.,]?$";
		Pattern p=Pattern.compile(pat);
		System.out.println(orig+": \n"+pat);
		Matcher m = p.matcher(orig);
		boolean b;
		if (m.matches()) {
			b= true;
		}
		else {
			b = false;
		}
		return b;
	}
	public  class Ask implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try{
				if (!questionA.getText().equals("")&&!answerA.getText().equals("")) {
					Question questions=new Question(questionA.getText());
					questions.question=questionA.getText();
					in = answerA.getText();
					writer.writeObject(questions);
					questionA.setEditable(false);	
					answerA.setEditable(false);
					askQ.setEnabled(false);
				}
				
			}
			catch(Exception ex){
				ex.printStackTrace();
			}	
			v=true;
		}
	}
}
// Cайт!!!
