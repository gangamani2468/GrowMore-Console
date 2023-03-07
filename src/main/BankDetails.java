package main;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.log4j.Level;

import users.signInProcess;

public class BankDetails {
	
	String bankName;
	String bankBranch;
	String IFSCCode;
	public Long BankAccountNumber;
	double Balance;
	Long phone;


	static Scanner sc=new Scanner(System.in); 
	
	public BankDetails(String bankName, String bankBranch, String iFSCCode, Long bankAccountNumber) {
//		super();
		this.bankName = bankName;
		this.bankBranch = bankBranch;
		IFSCCode = iFSCCode;
		BankAccountNumber = bankAccountNumber;
		

	}
	public BankDetails(String bankName, String bankBranch, String iFSCCode, Long bankAccountNumber,double balance,Long phone) {
//		super();
		this.bankName = bankName;
		this.bankBranch = bankBranch;
		IFSCCode = iFSCCode;
		BankAccountNumber = bankAccountNumber;
		this.Balance=balance;
		this.phone=phone;

	}
	public void addBankBalance(Double amt) {
		Balance+=amt;
	}
	
	public String getBankName() {
		return bankName;
	}



	public void setBankName(String bankName) {
		this.bankName = bankName;
	}



	public String getBankBranch() {
		return bankBranch;
	}



	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}



	public String getIFSCCode() {
		return IFSCCode;
	}



	public void setIFSCCode(String iFSCCode) {
		IFSCCode = iFSCCode;
	}



	public Long getBankAccountNumber() {
		return BankAccountNumber;
	}



	public void setBankAccountNumber(Long bankAccountNumber) {
		BankAccountNumber = bankAccountNumber;
	}



	public double getBalance() {
		return Balance;
	}



	public void setBalance(double balance) {
		Balance += balance;
	}
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	private static boolean isBank(String bankName) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering bank check method");  
		Iterator itr=MainClass.bankDetails.iterator();
		while(itr.hasNext()) {
			BankDetails bank=(BankDetails)itr.next();
			if(bank.bankName.equals(bankName)) {
				return true;
			}
		}
		return false;
	}
	private static boolean isBankBranch(String branch) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering bank branch check method");  
		Iterator itr=MainClass.bankDetails.iterator();
		while(itr.hasNext()) {
			BankDetails bank=(BankDetails)itr.next();
			if(bank.bankBranch.equals(branch)) {
				return true;
			}
		}
		return false;
	}
	private static boolean isIfscCode(String ifsc) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering ifsc code check method");  
		Iterator itr=MainClass.bankDetails.iterator();
		while(itr.hasNext()) {
			BankDetails bank=(BankDetails)itr.next();
			if(bank.IFSCCode.equals(ifsc)) {
				return true;
			}
		}
		return false;
	}
	private static boolean isAccountNumber(String accNumber) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering account number check method");  
		Iterator itr=MainClass.bankDetails.iterator();
		while(itr.hasNext()) {
			BankDetails bank=(BankDetails)itr.next();
			if(bank.BankAccountNumber.equals(Long.parseLong(accNumber))) {
				return true;
			}
		}
		return false;
	}
	private static boolean isVerfied(String bankName,String bankBranch,String ifsc,Long AccNumber,Long phonenumber) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering isVerfied bank method");  
		Iterator itr=MainClass.bankDetails.iterator();
		BankDetails bank=null;
		while(itr.hasNext()) {
			bank=(BankDetails)itr.next();
			if(bank.BankAccountNumber.equals(AccNumber)) {
				break;
			}
		}
		
	
		System.out.println(bank.phone);
		System.out.println(phonenumber);
		if(bank.bankName.equals(bankName)&&bank.bankBranch.equals(bankBranch)&&bank.IFSCCode.equals(ifsc)&&bank.BankAccountNumber.equals(AccNumber)&&bank.phone.equals(phonenumber)) {
			return true;
		}
		
			return false;
		
		
		
	}

	public static BankDetails BankDetailsCollect(Long phonenumber) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering bank details collect method");  
		String bankName;
		String BankBranch;
		String ifscCode;
		String AccountNumber;
		while(true) {
			
			while(true) {
				System.out.println("Bank Name:");
				bankName=sc.nextLine();
				if(isBank(bankName)) {
					break;
				}
			}
			while(true) {
				System.out.println("Bank branch:");
				BankBranch=sc.nextLine();
				if(isBankBranch(BankBranch)) {
					break;
				}
			}
			while(true) {
				System.out.println("IFSC Code:");
				ifscCode=sc.nextLine();
				if(isIfscCode(ifscCode)) {
					break;
				}
			}
			while(true) {
				System.out.println("AccountNumber:");
				AccountNumber =sc.nextLine();
				if(isAccountNumber(AccountNumber)) {
					break;
				}
			}
			if(isVerfied(bankName,BankBranch,ifscCode,Long.parseLong(AccountNumber),phonenumber)) {
				System.out.println("  /");
				System.out.println("\\/ SUCCESSFULLY VERIFIED ");
				break;
			}
			
		}
		return new BankDetails(bankName,BankBranch,ifscCode,Long.parseLong(AccountNumber));
	}



}
