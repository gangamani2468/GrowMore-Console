package users;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.log4j.Level;

import main.BankDetails;
import main.MainClass;

//import Enums.*;

public class DematAccount {
	public DematAccount( String name, Date date_Of_Birth, Long phone_Number, String email,
			String pAN_Number, String marital_Status, String gender, Long dematAccountNumber, String password,
			Long bankAccountNumber,double grow_More_Balance) {
//		super();
		BankAccountNumber = bankAccountNumber;
		Name = name;
		Date_Of_Birth = date_Of_Birth;
		Phone_Number = phone_Number;
		Email = email;
		PAN_Number = pAN_Number;
		Marital_Status = marital_Status;
		Gender = gender;
		DematAccountNumber = dematAccountNumber;
		this.password = password;
		Grow_More_Balance = grow_More_Balance;
	}
	public DematAccount( String name, Date date_Of_Birth, Long phone_Number, String email,
			String pAN_Number, String marital_Status, String gender, Long dematAccountNumber, String password,
			Long bankAccountNumber) {
//		super();
		BankAccountNumber = bankAccountNumber;
		Name = name;
		Date_Of_Birth = date_Of_Birth;
		Phone_Number = phone_Number;
		Email = email;
		PAN_Number = pAN_Number;
		Marital_Status = marital_Status;
		Gender = gender;
		DematAccountNumber = dematAccountNumber;
		this.password = password;
	
	}

	private Long BankAccountNumber;
	private String Name;
	private Date Date_Of_Birth;

	private Long Phone_Number;
	private String Email;
	private String PAN_Number;
	private String Marital_Status;
	private String Gender; 
	private Long DematAccountNumber;
	private String password;
	private static double Grow_More_Balance;



	

	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Date getDate_Of_Birth() {
		return Date_Of_Birth;
	}

	public void setDate_Of_Birth(Date date_Of_Birth) {
		Date_Of_Birth = date_Of_Birth;
	}

	public Long getPhone_Number() {
		return Phone_Number;
	}

	public void setPhone_Number(Long phone_Number) {
		Phone_Number = phone_Number;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPAN_Number() {
		return PAN_Number;
	}

	public void setPAN_Number(String pAN_Number) {
		PAN_Number = pAN_Number;
	}

	public String getMarital_Status() {
		return Marital_Status;
	}

	public void setMarital_Status(String marital_Status) {
		Marital_Status = marital_Status;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public Long getDematAccountNumber() {
		return DematAccountNumber;
	}

	public void setDematAccountNumber(Long dematAccountNumber) {
		DematAccountNumber = dematAccountNumber;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public Long getBankAccountNumber() {
		return BankAccountNumber;
	}

	public void setBankAccountNumber(Long bankAccountNumber) {
		BankAccountNumber = bankAccountNumber;
	}
	public double getGrow_More_Balance() {
		return Grow_More_Balance;
	}

	public void setGrow_More_Balance(double grow_More_Balance) {
		Grow_More_Balance = grow_More_Balance;
	}
	public static boolean setBalance(BankDetails bank,int addMoney) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering setBalance(dematAccount class) method");  
		 if(bank.getBalance()-100>addMoney) {
			 bank.setBalance(bank.getBalance()-addMoney);
			 Grow_More_Balance +=addMoney;
			 Iterator itr=MainClass.clients.iterator();
			 DematAccount account=null;
			 while(itr.hasNext()) {
				 account=(DematAccount) itr.next();
				 if(account.BankAccountNumber.equals(bank.getBankAccountNumber())) {
					 break;
				 }
			 }
			 PreparedStatement stmt;
				try {
					stmt = MainClass.dbConnection.prepareStatement("update DematAccount set grow_more_balance=? where BankAccountNumber=? ;");
					stmt.setString(1, ""+account.getGrow_More_Balance());
					stmt.setString(2, ""+bank.BankAccountNumber);
					stmt.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					MainClass.log.log(Level.ERROR, bank.BankAccountNumber+" "+LocalDateTime.now()+" Error Occured.", e);
				}
			 
			return true; 
		 }
		 else {
			 System.out.println("insufficient balance");
			 System.out.println("if you want add money in your bank account?\n1-->yes\n2-->no");
			 String option =MainClass.sc.nextLine();
			 PreparedStatement stmt;
			 if(option.equals("1")) {
				 
				 System.out.println("How much Add to your bank account?");
				String add;
				while(true) {
					add=MainClass.sc.nextLine();
					if(signInProcess.isNumeric(add)) {
						break;
					}
					System.out.println("please enter valid input");
				}
				bank.setBalance(Double.parseDouble(add));
					try { stmt = MainClass.dbConnection.prepareStatement("update BankDetails set balance=? where BankAccountNumber="+"\""+bank.getBankAccountNumber()+"\""+" ;");
						stmt.setString(1, ""+bank.getBalance());
						
						stmt.execute();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
					}
					 setBalance(bank,addMoney);
			 }
			 
			 return false;
		 }
	 }
	public void addMoney(Double amt) {
		Grow_More_Balance += amt;
	}

}
