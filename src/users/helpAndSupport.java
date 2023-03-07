package users;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.apache.log4j.Level;

import main.MainClass;

public class helpAndSupport {
	static Scanner sc=new Scanner(System.in);
	public static void helpMain() {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering helpAndSupportMain method");  
		
		a: while(true) {
			System.out.println("Select help topic\n1-->Stocks\n2-->Payments\n3-->Contact Us(raise Ticket)");
			String select=sc.nextLine();
			switch(select) {
			case "1":
				Stocks();
				break;
			case "2":
				Payments();
				break;
			case "3":
				raiseTicket();
				break;
			default:
				break a;
			
			}	
		}
		
	}
	private static void Stocks() {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering Stocks(helpAndSupport) method");  
		
		e:while(true) {
			System.out.println("FAQ\noptions");
			System.out.println("1-->What is an auction? When will the auction bill be updated?\n2-->When will I get the money after selling stocks?\n3-->you raise any Ticket?\nany key-->continue");
			String option=sc.nextLine();
			switch(option) {
			case "1":
				System.out.println("Auction happens when you sell positions you don’t actually have. If this happens, you will be charged a penalty (decided by exchange). The auction bill is updated at the end of 2 trading days after selling such positions");
				break;
			case "2":
				System.out.println("As per SEBI regulations, only 80% of the delivery sell amount is made available to invest immediately after a sell transaction, while the remaining 20% will be added to your Groww Balance and made available to invest on the next trading day (T+1). You will be able to withdraw the whole sell amount after 9 PM on T+2 day");
				break;
			case "3":
				raiseTicket();
				break;
			default:
				break e;
				
			}
			
		}
		}
	private static void Payments() {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering payments(helpAndSupport) method");  
		
		e:while(true) {
			System.out.println("FAQ\noptions");
			System.out.println("1-->Why do I have negative balance?\n2-->What is Refund to bank?\n3-->you raise any Ticket?\nany key-->continue");
			String option=sc.nextLine();
			switch(option) {
			case "1":
				System.out.println("Your Grow More balance could be negative because:\n\nYou did not have enough balance for a trade, and your order got executed at a higher price due to sudden price movement (happens in case of market orders), or\nYou did not have enough balance to pay for DP charges (deducted when shares are debited from your Demat account).\nTo avoid penalties, add funds to your Groww balance when it is negative");
				break;
			case "2":
				System.out.println("As per SEBI guidelines, funds kept in Grow More Balance will be refunded to your bank account on the first Friday of every month or every quarter based on the settlement period (30 or 90 days respectively) chosen by you while creating a Grow More account.\nAlternatively, in case of inactivity in your account for 30 days, funds in your Grow More balance will be refunded to your bank account.\nThis is known as known as refund to bank.\nTo know more about the same please click on the button below.");
				break;
			case "3":
				raiseTicket();
				break;
			default:
				break e;
				
			}
			
		}
		}
	public static void raiseTicket() {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering raiseTicket(helpAndSupport) method");  
		
		Socket s=null;;
		DataInputStream din=null;;
		DataOutputStream dout=null;;
		try {
			
			s=new Socket("localhost",56244);  
			din=new DataInputStream(s.getInputStream());  
			dout=new DataOutputStream(s.getOutputStream());  
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
			System.out.println("if you want exit? press 0");
			String str="",str2="";  
			while(!str.equals("0")){  
				str=br.readLine();  
				dout.writeUTF(str);  
				dout.flush();  
				str2=din.readUTF();  
				System.out.println("Grow More: "+str2);  
				
			}  
			  
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
		}  
		finally {
		try {
			din.close();
			s.close();  
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
			MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
		}  
		
		}
	}

}
