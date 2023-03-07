package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

import org.apache.log4j.Level;
public class AdminServer {

	


	

		public static void main(String args[]){
			MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering server hosting");  
			ServerSocket ss=null;
			Socket s =null;
			DataInputStream din=null;
			DataOutputStream dout=null;
			BufferedReader br=null;
			try {
				ss = new ServerSocket(56244);
				s=ss.accept();  
				din=new DataInputStream(s.getInputStream());  
				dout=new DataOutputStream(s.getOutputStream());  
				br=new BufferedReader(new InputStreamReader(System.in));  
				String str="",str2="";  
				while(!str.equals("0")){  
				str=din.readUTF();  
				System.out.println("user: "+str);  
				str2=br.readLine();  
				dout.writeUTF(str2);  
				dout.flush();  
				}    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			finally {
				try {
					din.close();
					s.close();  
					ss.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				
			}
			
			  
			

	}

}
