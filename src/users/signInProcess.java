package users;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Level;

import main.BankDetails;
import main.MainClass;

public class signInProcess {
	static Scanner sc=new Scanner(System.in);
	public signInProcess() {
		// TODO Auto-generated constructor stub
	}
public static DematAccount signUp(){
		
//		String client="";
	
	 MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering signup method");  
			System.out.println("Enter your full name");
			String name=sc.nextLine();
			String Date_Of_Birth;
			String phoneNumber;
			String email;
			String maritalStatus;
			String gender;
			String pan;
			String pass;
			Long dematAccountNumber;
			while(true) {
				System.out.println("Enter your Date of Birth(DD/MM/YYYY)");
				String date= sc.nextLine();
				 Date_Of_Birth=date;
				try {
					if(isValidDate(date)) {
						 
						break;
					}
					else {
						System.out.println(" please enter valid input");
					
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
				}
			}
			p:
			while(true) {
				System.out.println("Enter a phone number");
				phoneNumber=sc.nextLine();
				if(phoneNumber.length()==10&&isNumeric(phoneNumber)) {
					Iterator sc=MainClass.clients.iterator();
					while(sc.hasNext()) {
						DematAccount data=(DematAccount)sc.next();
						if(data.getPhone_Number().equals(phoneNumber)) {
							System.out.println("this number already exists ");
							continue p;	
						}		
					}
					break;
				}
				else {
					System.out.println("please enter valid input");
					
				}
			}
			e:
			while(true) {
				System.out.println("Enter a Gmail address");
				email=(sc.nextLine()).toLowerCase();
				if(email.contains("@gmail.com")) {
					Iterator sc=MainClass.clients.iterator();
					while(sc.hasNext()) {
						DematAccount data=(DematAccount)sc.next();
						if(data.getEmail().equals(email)) {
							System.out.println("this mail already exists ");
							continue e;	
						}	
					
					}
					break;
				}
				
				else {
					System.out.println(" please enter valid input");
					
				}
			}
			while(true) {
				System.out.println("Enter pan number");
				pan=sc.nextLine();
				if(pan.length()==10) {
					break;}
				else {
					System.out.println("please enter valid input");
				}
			}
			while(true) {
				System.out.println("Enter Marital Status");
				System.out.println("option \n1-->Single\n2-->Married ");
				maritalStatus=sc.nextLine();
				if(maritalStatus.equals("1")||maritalStatus.equals("2")) {
					if(maritalStatus.equals("1")){
						maritalStatus="Single";
					}
					else {
						maritalStatus="Married";
					}
					break;
				}
				else {
					System.out.println("please enter valid input");
				}
			}
			while(true) {
				System.out.println("Enter Gender");
				System.out.println("option \n1-->Male\n2-->Female\n-->Others ");
				gender=sc.nextLine();
				if(gender.equals("1")||gender.equals("2")) {
					if(gender.equals("1")){
						gender="Male";
					}
					else {
						gender="Female";
					}
					break;
				}
				else {
					System.out.println("please enter valid input");
				}
			}
			x:
			while(true) {
				dematAccountNumber=(long)(Math.random()*1000000000000000l)+1600000000000000l; 
				Iterator sc=MainClass.clients.iterator();
				while(sc.hasNext()) {
					DematAccount data=(DematAccount)sc.next();
					if(data.getDematAccountNumber().equals(dematAccountNumber)) {
					continue x;	
					}		
				}
				
				break;
			}
			while(true) {
				System.out.println("Enter the Password(Do not use spaces, it will be eliminated.)");
				System.out.println("8-16 characters use");
				pass=sc.nextLine();
				if(pass.length()>7&&17>pass.length()) {
					break;
				}
				System.out.println("please use 8 to 16 characters");
			}
			
			while(true) {
				String captcha=captchaGeneration();
				System.out.println("Check Captcha.."+captcha);
				String captchaCheck=sc.nextLine();
				if(captcha.equals(captchaCheck)) {
					break;
				}else {
					System.err.println("ReEnter Captcha...");
				}
			
			}
		BankDetails accountDetails=	BankDetails.BankDetailsCollect(Long.parseLong(phoneNumber));
		
		
		
		String client = name+","+Date_Of_Birth+","+phoneNumber+","+email+","+pan+","+maritalStatus+","+gender+","+dematAccountNumber.toString()+","+pass.replaceAll(" ","");
		
		MainClass.clients.add(new DematAccount(name,new Date(Date_Of_Birth),Long.parseLong(phoneNumber),email,pan,maritalStatus,gender,dematAccountNumber,pass,accountDetails.getBankAccountNumber()));
		
		return new DematAccount(name,new Date(Date_Of_Birth),Long.parseLong(phoneNumber),email,pan,maritalStatus,gender,dematAccountNumber,pass,accountDetails.getBankAccountNumber());
		
	}
	public static boolean isNumeric(String str) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering valid number check method");  
	try {
		Double.parseDouble(str);
		return true;
	}
	catch(Exception e){
		MainClass.log.log(Level.ERROR," "+LocalDateTime.now()+" Error Occured.", e);
		return false;
	}
	}
	public static String captchaGeneration() {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering captcha generate method");  
		int n=7;
	Random rd=new Random(63);
	String ch="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@";
	String captcha="";
	while(n-->0) {
		int index=(int)(Math.random()*63);
		captcha+=ch.charAt(index);
		
	}
	return captcha;
}
	private static boolean isValidDate( String date) throws ParseException {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering valid date check method");  
	boolean valid = false;

    try {
    	  SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/YYYY");
    	  Date Date = sdfrmt.parse(date);
    	  date=sdfrmt.toLocalizedPattern();
     

        valid = true;

    } catch (DateTimeParseException e) {
		MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
        valid = false;
    }

    return valid;
}

}
