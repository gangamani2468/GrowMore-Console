package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.log4j.Level;

import Shares.Holdings;
import Shares.ShareInfo;
import users.DematAccount;
import users.signInProcess;

public class BuyAndSell {
	public static Scanner sc=new Scanner(System.in);
	static DecimalFormat fmt=new DecimalFormat("#.##");
	public BuyAndSell() {
		// TODO Auto-generated constructor stub
	}
	public void Buy(ShareInfo info,DematAccount account,String select,String isin) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering buy method");  
		
		Double TotalAmount;
		String Quantity;
		Integer qty;
		while(true) {
			System.out.println("How Many Quantity You Want?");
			Quantity=sc.nextLine();
			if(signInProcess.isNumeric(Quantity)) {
				qty=Integer.parseInt(Quantity);
				if(qty<20001) {
					TotalAmount=qty*Double.parseDouble(info.getSharePrice().toString());
					break;					
				}
				continue;
			}
			else {
				continue;
			}
			
		}
		
		String curr= ""+MainClass.marketPriceCheck(isin).get("ltp");
		Double  currPrice1 = Double.valueOf(curr);
		
//		Double currPrice=Double.parseDouble(currPrice1);
		Double currTotalPrice=Double.parseDouble(fmt.format(currPrice1*qty));
		System.out.println("Total amount:"+currTotalPrice);
		if(currTotalPrice>account.getGrow_More_Balance()) {
			System.out.println("you have insufficient balance");
			System.out.println("if you want to add money?\n1-->yes\n2-->no ");
			String option=sc.nextLine();
			
			if(option.equals("1")) {
				addDematBalance(account);
						
					
				
			}
		}
		else {
		if(alreadyBuyThisStock(isin,account)){
			System.out.println("update...");
			Holdings holds=alreadyBuyStock(account,isin,TotalAmount,qty,select,info,currTotalPrice);
			
			Holdings hold=new Holdings(holds.getDematAccountNumber(),holds.getShareName(),holds.getSharePrice(),fmt.format((holds.getInvestedAmount()+TotalAmount)/(holds.getShareQuantity()+qty)),holds.getShareQuantity()+qty,profitAndLoss(holds.getInvestedAmount()+TotalAmount,isin,qty+holds.getShareQuantity()),Double.parseDouble(fmt.format(holds.getInvestedAmount()+TotalAmount)),Double.parseDouble(fmt.format(holds.getCurrentAmount()+currTotalPrice)),isin);
			Iterator itr1=MainClass.portfolio.iterator();
			Holdings portfolio=null;
			while(itr1.hasNext()) {
				portfolio=(Holdings) itr1.next();
				if(portfolio.getISIN().equals(isin)&&portfolio.getDematAccountNumber().equals(account.getDematAccountNumber())) {
					break;
					}
			}
			MainClass.portfolio.remove(portfolio);
			MainClass.portfolio.add(hold);
			account.setGrow_More_Balance(account.getGrow_More_Balance()-TotalAmount);
			try {
				PreparedStatement stmt1=MainClass.dbConnection.prepareStatement("update DematAccount set grow_more_balance=? where bankAccountNumber=?");
				stmt1.setString(1, ""+account.getGrow_More_Balance());
				stmt1.setString(2, ""+account.getBankAccountNumber());			
				stmt1.execute();
				PreparedStatement stmt=MainClass.dbConnection.prepareStatement("update Holdings set ShareAvgPrice=? ,ShareQuantity=? ,InvestedAmount=? ,currentAmount=? ,profitAndLoss=?  where ISIN_Number=\""+hold.getISIN()+"\" and DematAccountNumber=\""+hold.getDematAccountNumber().toString()+"\";");
				
			
				stmt.setString(1, hold.getShareAvgPrice().toString());
				stmt.setString(2,hold.getShareQuantity().toString());
				
				stmt.setString(3, hold.getInvestedAmount().toString());
				stmt.setString(4, hold.getCurrentAmount().toString());
				stmt.setString(5, hold.getProfitAndLoss());
				stmt.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				MainClass.log.log(Level.ERROR, hold.getDematAccountNumber()+" "+LocalDateTime.now()+" Error Occured.", e);

			}
		}
		else {
			System.out.println("new Enter....");
			Holdings hold=new Holdings(account.getDematAccountNumber(),info.getShareName(),(Object)info.getSharePrice(),(Object)fmt.format(TotalAmount/qty),qty,profitAndLoss(TotalAmount,isin,qty),TotalAmount,currTotalPrice,isin);
			account.setGrow_More_Balance(account.getGrow_More_Balance()-TotalAmount);
			MainClass.portfolio.add(hold);
			try {
				PreparedStatement stmt1=MainClass.dbConnection.prepareStatement("update DematAccount set grow_more_balance=? where bankAccountNumber=?");
				stmt1.setString(1, ""+account.getGrow_More_Balance());
				stmt1.setString(2, ""+account.getBankAccountNumber());			
				stmt1.execute();
				PreparedStatement stmt=MainClass.dbConnection.prepareStatement("insert into Holdings values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, hold.getDematAccountNumber().toString());
				stmt.setString(2, hold.getShareName());
				stmt.setString(3, hold.getSharePrice().toString());
				stmt.setString(4, hold.getShareAvgPrice().toString());
				stmt.setString(5, hold.getShareQuantity().toString());
				stmt.setString(6, hold.getProfitAndLoss());
				stmt.setString(7, hold.getInvestedAmount().toString());
				stmt.setString(8, hold.getCurrentAmount().toString());
				stmt.setString(9, hold.getISIN());
				stmt.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				MainClass.log.log(Level.ERROR, hold.getDematAccountNumber()+" "+LocalDateTime.now()+" Error Occured.", e);
			}
		}
		}
		
	}
	public static String profitAndLosses(Double TotalAmount,String isin,Integer qty) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering profitAndLosses method");  
		return TotalAmount<currAmount(isin,qty)?"+"+fmt.format(currAmount(isin,qty)-TotalAmount):"-"+fmt.format(TotalAmount-currAmount(isin,qty));
	}
	private static String profitAndLoss(Double TotalAmount,String isin,Integer qty) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering profitAndLoss method");  
		String currPrice1=""+ MainClass.marketPriceCheck(isin).get("ltp");
		Double currPrice=Double.valueOf(currPrice1);
		Double currTotalPrice=currPrice*qty;
		Double profitAndLoss=TotalAmount>currTotalPrice?Double.parseDouble(fmt.format(TotalAmount-currTotalPrice)):Double.parseDouble(fmt.format(currTotalPrice-TotalAmount));
		
		return TotalAmount>currTotalPrice?"-"+profitAndLoss:"+"+profitAndLoss;
	}
	public static Double currAmount(String isin,Integer qty) {
//		
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering currAmount method");  
		String currPrice=""+ MainClass.marketPriceCheck(isin).get("ltp");
		Double currTotalPrice=Double.valueOf(currPrice)*qty;
		return Double.parseDouble(fmt.format(currTotalPrice));
	}
	private static boolean alreadyBuyThisStock(String isin,DematAccount account) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering alreadyBuyThisStock method");  
		System.out.println(MainClass.portfolio.size());
		Iterator itr=MainClass.portfolio.iterator();
//		System.out.println(itr.hasNext());
		while(itr.hasNext()) {
			Holdings hold=(Holdings) itr.next();
			System.out.println(hold.getISIN());
			System.out.println(hold.getDematAccountNumber());
			if(hold.getISIN().equals(isin)&&hold.getDematAccountNumber().equals(account.getDematAccountNumber())) {
				return true;
				}
		}
		return false;
				
	}
	private static Holdings alreadyBuyStock(DematAccount account,String isin,Double TotalAmount,Integer qty,String select,ShareInfo info,Double currTotalPrice) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering alreadyBuyStock method");  
		Iterator itr=MainClass.portfolio.iterator();
		while(itr.hasNext()) {
			Holdings hold=(Holdings) itr.next();
			if(hold.getISIN().equals(isin)&&hold.getDematAccountNumber().equals(account.getDematAccountNumber())) {
				return hold;
				}
		}
		return null;	
	}
	public void Sell(ShareInfo info,DematAccount account,String select,String isin) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering sell method");  
		Iterator itr=MainClass.portfolio.iterator();
		System.out.println("selling");
		
		
		String sell;
		while(true) {
			System.out.println("how many shares you want to sell it?");
			sell=sc.nextLine();
			if(signInProcess.isNumeric(sell)) {
				break;
			}
			System.out.println("please enter valid input!");
		}
		Integer sells=Integer.parseInt(sell);
		while(itr.hasNext()) {
			Holdings hold=(Holdings)itr.next();
			if(hold.getISIN().equals(isin)&&hold.getDematAccountNumber().equals(account.getDematAccountNumber())) {
				if(hold.getShareQuantity()>sells) {
					Double amount=currAmount(isin,sells);
					System.out.println("invested"+"\t \t Current"+"\t \t P&L\n"+(hold.getInvestedAmount()/hold.getShareQuantity())*sells+"\t \t \t"+amount+"\t \t \t"+profitAndLoss((hold.getInvestedAmount()/hold.getShareQuantity())*sells,hold.getISIN(),sells));
					hold.setInvestedAmount(hold.getInvestedAmount()-((hold.getInvestedAmount()/hold.getShareQuantity())*sells));
					hold.setShareQuantity(hold.getShareQuantity()-sells);
					hold.setCurrentAmount(hold.getCurrentAmount()-amount);
					System.out.println(hold.getInvestedAmount()+"");
					account.addMoney(amount);
					try {
						PreparedStatement stmt1=MainClass.dbConnection.prepareStatement("update dematAccount set grow_more_balance=? where DematAccountNumber=?");
						stmt1.setString(1, ""+account.getGrow_More_Balance());
						stmt1.setString(2,""+account.getDematAccountNumber());
						
					PreparedStatement stmt=MainClass.dbConnection.prepareStatement("update Holdings set ShareAvgPrice=? ,profitAndLoss=? ,InvestedAmount=? ,currentAmount=?,ShareQuantity=?   where ISIN_Number=\""+hold.getISIN()+"\" and DematAccountNumber=\""+hold.getDematAccountNumber().toString()+"\";");
					Double total=(hold.getShareQuantity()-sells)*Double.parseDouble(""+hold.getSharePrice());
					stmt.setString(1, hold.getShareAvgPrice().toString());
					stmt.setString(2,fmt.format(Double.valueOf(profitAndLoss(total,isin,hold.getShareQuantity()-sells))));
					
					stmt.setString(3, fmt.format(Double.valueOf(""+hold.getInvestedAmount())));
					stmt.setString(4, fmt.format(Double.valueOf(""+hold.getCurrentAmount())));
					stmt.setString(5, ""+hold.getShareQuantity());
					stmt.executeUpdate();
					stmt1.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					MainClass.log.log(Level.ERROR, hold.getDematAccountNumber()+" "+LocalDateTime.now()+" Error Occured.", e);
				}
				}
			else if(hold.getShareQuantity()==sells){
				
					System.out.println("invested"+"\t \t Current"+"\t \t P&L\n"+hold.getInvestedAmount()+"\t \t \t"+currAmount(isin,hold.getShareQuantity())+"\t \t \t"+profitAndLoss(hold.getInvestedAmount(),hold.getISIN(),hold.getShareQuantity()));
					account.setGrow_More_Balance(account.getGrow_More_Balance()+currAmount(isin,hold.getShareQuantity()));
					hold.setCurrentAmount(0.0);
					hold.setInvestedAmount(0.0);
					hold.setShareQuantity(0);
					try {
						PreparedStatement stmt1=MainClass.dbConnection.prepareStatement("update DematAccount set grow_more_balance=? where DematAccountNumber=\""+hold.getDematAccountNumber().toString()+"\";");
						stmt1.setString(1, ""+(account.getGrow_More_Balance()));
						stmt1.execute();
						PreparedStatement stmt=MainClass.dbConnection.prepareStatement("delete from Holdings where ISIN_Number=\""+hold.getISIN()+"\" and DematAccountNumber=\""+hold.getDematAccountNumber().toString()+"\";");
						stmt.execute();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						MainClass.log.log(Level.ERROR, hold.getDematAccountNumber()+" "+LocalDateTime.now()+" Error Occured.", e);
					}
				}
			else {
				System.out.println("please give valid input!");
			}
				}
		}
	
		}
	public static void addDematBalance(DematAccount account) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering addDematBalance method");  
		while(true) {
			System.out.println("if you want exit press 0  \nEnter any key to continue... ");
			String exit=sc.nextLine();
			if(exit.equals("0")) {
				break;
			}
		
		String amt;
		while(true) {
			System.out.println("how much amount you want?");
			amt=sc.nextLine();
			if(signInProcess.isNumeric(amt)) {
				break;
			}
			System.out.println("please enter valid input");
		}
		
		BankDetails bank=null;
		Iterator itr=MainClass.bankDetails.iterator();
		while(itr.hasNext()) {
			bank=(BankDetails)itr.next();
			if(bank.BankAccountNumber.equals(account.getBankAccountNumber())) {
				break;
			}}
			if(DematAccount.setBalance(bank,Integer.parseInt(amt))) {
				
				System.out.println("₹"+amt+" Payment Successfully");
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
			}}}	

}
