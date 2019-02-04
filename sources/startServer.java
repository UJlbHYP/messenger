package com.ilnur.server;
import java.io.*;
import java.net.*;
import java.util.*;
public class startServer  {
	ArrayList<ObjectOutputStream> clientOutputs;
	public static void main(String[] args) {
		System.out.print("!");
		
	}
	public class ClientHandler implements Runnable {
		ObjectInputStream inp;
		Socket clientSocket;
		public ClientHandler(Socket socket){
			try{
				clientSocket=socket;
				inp=new ObjectInputStream(clientSocket.getInputStream());
			}
			catch(Exception e ){
				e.printStackTrace();
			}
		}
		public void run(){
			Object messLetter="";
			try {

				while ((messLetter=inp.readObject())!=null){
					toClientsChat(messLetter);
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void go(int port){
		clientOutputs = new ArrayList<ObjectOutputStream>();
		ServerSocket ServSocket;
			try{
				ServSocket=new ServerSocket(port);
				while(true){
					Socket clientSocket = ServSocket.accept();
					ObjectOutputStream out=new ObjectOutputStream(clientSocket.getOutputStream());
					clientOutputs.add(out);
					Thread ch = new Thread(new ClientHandler(clientSocket));
					ch.start();
					System.out.print("Connecr");
				}
			}
			catch(Exception e ){
				e.printStackTrace();
			}
	}
	public void toClientsChat(Object one){
		Iterator<ObjectOutputStream> it = clientOutputs.iterator();
		it = clientOutputs.iterator();
			while(it.hasNext()){
				try{
					ObjectOutputStream out = (ObjectOutputStream) it.next();
					out.writeObject(one);

				}
				catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

}