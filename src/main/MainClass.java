package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Shares.Holdings;
import Shares.ShareInfo;
import users.*;


public class MainClass {
	public static List<Holdings> portfolio=new ArrayList<Holdings>();
	public static List<DematAccount> clients=new ArrayList<DematAccount>();
	public static List<BankDetails> bankDetails=new ArrayList<BankDetails>();
	public static DematAccount currentlyLoginClient=null;
	public static BankDetails currentlyLoginBank=null;
	public static Scanner sc=new Scanner(System.in);
	public static Connection dbConnection;
	
	
	public static Logger log = Logger.getLogger(MainClass.class.getName()); 
	static SimpleDateFormat sdft = new SimpleDateFormat("DD/MM/YYYY");
	static{
		try {
			getDBConnection();
			
		if(dbConnection == null) {
			System.out.println("FATAL: Please check Data Base Connection Issue");
			System.exit(0);
		}
		
			Statement st = dbConnection.createStatement();
			ResultSet holdings=st.executeQuery("select * from Holdings;");
			while(holdings.next()) {
				portfolio.add(new Holdings(Long.parseLong(holdings.getString(1)),holdings.getString(2),(Object)holdings.getString(3),(Object)holdings.getString(4),Integer.parseInt(holdings.getString(5)),holdings.getString(6),Double.parseDouble(holdings.getString(7)),Double.parseDouble(holdings.getString(8)),holdings.getString(9)));
			}
			ResultSet bank=st.executeQuery("select *from BankDetails");
			while(bank.next()) {
				bankDetails.add(new BankDetails(bank.getString(1),bank.getString(2),bank.getString(3),Long.parseLong(bank.getString(4)),Double.parseDouble(bank.getString(5)),Long.parseLong(bank.getString(6))));
			}
			ResultSet dematAccount=st.executeQuery("select*from DematAccount order by name;");
			while(dematAccount.next()) {
				clients.add(new DematAccount(dematAccount.getString(1),new Date(dematAccount.getString(2)),Long.parseLong(dematAccount.getString(3)),dematAccount.getString(4),dematAccount.getString(5),dematAccount.getString(6),dematAccount.getString(7),Long.parseLong(dematAccount.getString(8)),dematAccount.getString(9),Long.parseLong(dematAccount.getString(10)),Double.parseDouble(dematAccount.getString(11))));
			}
			
			
	}
		catch(Exception e) {
			MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
		}}

//	public static List<DematAccount> clients=new ArrayList<DematAccount>();
	
	public static void main(String[] args) {
		
	      log.log(Level.DEBUG," "+LocalDateTime.now()+" entering main method");  
		System.out.println("---------------------------------------------------------------------welcome to Grow More-----------------------------------------------------------------------");
		
		
		a:while(true) {
			System.out.println("option 1-->Sign In(you have demat account)\n2-->Sign Up(you haven't demat account)\nany other option click-->exit");
			String choose=sc.nextLine();
			switch(choose) {
				case "1":
					while(true) {
					System.out.println("Enter phone number:");
					String phone=sc.nextLine();
					System.out.println("Enter password");
					String pass=sc.nextLine();
//					if()
					if(signIn(phone,pass)) {
//						accessClients();
						break;
					}
					else {
						System.out.println("you given input is wrong!!!");
					}
					}
					
					break;
				case "2":
//					String header= "Name"+","+"Date of Birth"+","+"Phone Number"+","+"Email"+","+"Pan Number"+","+"Marital Status"+","+"Gender"+","+"Demat AccountNumber"+","+"Password";
					DematAccount Data= signInProcess.signUp();
					
				try {
					PreparedStatement stmt=dbConnection.prepareStatement("insert into DematAccount values(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
					stmt.setString(1, Data.getName());
					
					stmt.setString(2,sdft.format(Data.getDate_Of_Birth()));
					stmt.setString(3, Data.getPhone_Number().toString());
					stmt.setString(4, Data.getEmail());
					stmt.setString(5, Data.getPAN_Number());
					stmt.setString(6, Data.getMarital_Status());
					stmt.setString(7, Data.getGender());
					stmt.setString(8, Data.getDematAccountNumber().toString());
					stmt.setString(9, Data.getPassword());
					stmt.setString(10,Data.getBankAccountNumber().toString());
					stmt.setString(11, ""+0);
					stmt.executeUpdate();
					
					} catch (SQLException e) {
					// TODO Auto-generated catch block
						MainClass.log.log(Level.ERROR, Data.getDematAccountNumber()+" "+LocalDateTime.now()+" Error Occured.", e);
					
					}
					break;
				default:
					continue a;
					
					
//					FileWriter dataBase=new FileWriter(file);
//					BufferedWriter db=new BufferedWriter(dataBase);
					
			}
		}
		
		
		
	}
	private static boolean signIn(String phone,String pass) {
		 log.log(Level.DEBUG," "+LocalDateTime.now()+" entering signin method");  
		Iterator sc=clients.iterator();
		while (sc.hasNext()) {
			DematAccount data=(DematAccount) sc.next();
			
				
				
				if((data.getPhone_Number().toString()).equals(phone)&&data.getPassword().equals(pass)) {
					System.out.println("Succesfully Signed In..");
					currentlyLoginClient=new DematAccount(data.getName(),data.getDate_Of_Birth(),data.getPhone_Number(),data.getEmail(),data.getPAN_Number(),data.getMarital_Status(),data.getGender(),data.getDematAccountNumber(),data.getPassword(),data.getBankAccountNumber(),data.getGrow_More_Balance());
					
					accessClients(currentlyLoginClient);
					return true;
					
				}
			
		}
		
		return false;
	}
	
	
	
	private static void accessClients(DematAccount account) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering accessClients method");  
		
		while(true) {
			System.out.println("Option \n1-->Watchlist\n2-->Portfolio\n3-->Profile\n4-->Support\n5-->Exit");
			String option=sc.next();
			if(option.equals("1")||option.equals("2")||option.equals("3")||option.equals("4")) {}
			else {
				continue;
			}
			switch(option){
				case "1":
					watchList(account);
					break;
				case "2":
					portfolio(account);
					break;
				case "3":
					profile(account);
					break;
				case "4":
					Support();
					break;
				case "5":
					break;
			}
			break;
		}
		
		System.out.println("kkk");

	}
	private static void watchList(DematAccount account) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering watchList method");  
		a:
		while(true) {
		System.out.println("Shares info\n1-->ONGC\n2-->TATAPOWER\n3-->TCS\n4-->WIPRO\n5-->ZOMATO\n6-->ITC\n7-->SBI\n8-->NATCO PHARMA\n9-->TATA MOTORS");
		sc.nextLine();
		String select=sc.nextLine();
		Map<String,String> sharesList=new HashMap<String,String>();
		sharesList.put("1","ONGC");
		sharesList.put("2","TATAPOWER");
		sharesList.put("3","TCS");
		sharesList.put("4","WIPRO");
		sharesList.put("5","ZOMATO");
		sharesList.put("6","ITC");
		sharesList.put("7","SBI");
		sharesList.put("8","NATCO PHARMA");
		sharesList.put("9","TATA MOTORS");
		
		String ISIN=ISINNumber(select);
		JSONObject jsonObj=marketPriceCheck(ISIN);
	
		if(jsonObj==null) {
			continue a;
		}
		
		
		
		ShareInfo info=new ShareInfo(sharesList.get(select),jsonObj.get("ltp"),jsonObj.get("low"),jsonObj.get("high"));
		System.out.println("--------------------------------------------------------------------"+info.getShareName()+"----------------------------------------------------------------------- ");
	
		System.out.println("\t                  ");
		System.out.println("\t                /"+"\\"+"");
		System.out.println("\t               /  ");
		System.out.println("\t          __  /   ");
		System.out.println("\t        _/  "+"\\"+"/    ");
		System.out.println("\t    ___/          ");
		System.out.println("\t   /             ");
		System.out.println("\t‾‾‾             ");
		System.out.println();
		System.out.println("Share Price:"+info.getSharePrice());
		System.out.println("Today Low:"+info.getTodayLow()+"\t \t \t \t Today High:"+info.getTodayHigh());
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		c:  while(true) {
		boolean check=false;
		double totalInvested=0;
		double totalCurrent=0;
		Iterator itr2=portfolio.iterator();
		while(itr2.hasNext()) {
			Holdings hold=(Holdings) itr2.next();
			if(hold.getDematAccountNumber().equals(account.getDematAccountNumber())&&hold.getISIN().equals(ISIN)) {
				check=true;
				totalInvested+=hold.getInvestedAmount();
				double currentAmount=BuyAndSell.currAmount(hold.getISIN(),hold.getShareQuantity());
				totalCurrent+=currentAmount;
				System.out.printf(""+hold.getShareName()+"\t\t\t\tAvg:"+hold.getShareAvgPrice()+"\t\t\t\t"+hold.getShareQuantity()+" Qty"+"\t\t\t\t\t\t\t\t\t P&L:"+BuyAndSell.profitAndLosses(hold.getInvestedAmount(),hold.getISIN(),hold.getShareQuantity())+"\n\n invested: "+hold.getInvestedAmount()+"\t\t\t\t\t \t\t\t\t\t\t\t\t\t\t\t current:"+currentAmount+"\n");
				System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			}
		}
		
			System.out.println("option if you want buy/sell this stock?");
			
			System.out.println("1-->Buy\n2-->Sell\n3-->continue");
			String choose=sc.nextLine();
			BuyAndSell bs=new BuyAndSell();
			switch(choose) {
				case "1":
					bs.Buy(info,account,select,ISIN);
					break;
				case "2":
					if(check) {
						bs.Sell(info,account,select,ISIN);
					}
					else {
						System.out.println("please enter valid input!");
						break c;
					}
					
					break;
				case "3":
					break c;
				
			}
			
		}
		
		break;
		}//if close
		
		
//		
	}//loop close
	
	
	private static void portfolio(DematAccount account) {
//		
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering portfolio method");  
		System.out.printf("%80s"," Holdings\n");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		boolean check=false;
		double totalInvested=0;
		double totalCurrent=0;
		Iterator itr2=portfolio.iterator();
		while(itr2.hasNext()) {
			Holdings hold=(Holdings) itr2.next();
			if(hold.getDematAccountNumber().equals(account.getDematAccountNumber())) {
				check=true;
				totalInvested+=hold.getInvestedAmount();
				double currentAmount=BuyAndSell.currAmount(hold.getISIN(),hold.getShareQuantity());
				totalCurrent+=currentAmount;
				System.out.printf(""+hold.getShareName()+"\t\t\t\tAvg:"+hold.getShareAvgPrice()+"\t\t\t\t"+hold.getShareQuantity()+" Qty"+"\t\t\t\t\t\t\t\t\t P&L:"+BuyAndSell.profitAndLosses(hold.getInvestedAmount(),hold.getISIN(),hold.getShareQuantity())+"\n\n invested: "+hold.getInvestedAmount()+"\t\t\t\t\t \t\t\t\t\t\t\t\t\t\t\t current:"+currentAmount+"\n");
				System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				}
		}
		if(check) {
			DecimalFormat fmt=new DecimalFormat("#.##");
			String pAndL=totalInvested>totalCurrent?"-"+fmt.format(totalInvested-totalCurrent):"+"+fmt.format(totalCurrent-totalInvested);
			System.out.println("Total Invested"+"\t\t\t\t\t\t\t\tP&L\t\t\t\t\t\t\t\t\tCurrent Amount\n");
			System.out.println(fmt.format(totalInvested)+"\t\t\t\t\t\t\t\t\t"+pAndL+"\t\t\t\t\t\t\t\t\t"+fmt.format(totalCurrent));
		}
		if(!check) {
			System.out.println("you have No holdings");
		}
	}
	private static void profile(DematAccount account) {
//		
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering profile method");  
		System.out.println("Name:"+account.getName());
		System.out.println("Phone number:"+account.getPhone_Number());
		System.out.println("Grow More Balance:"+account.getGrow_More_Balance());
		System.out.println("Email:"+account.getEmail());
		System.out.println("DematAccountNumber:"+account.getDematAccountNumber());
		System.out.println("BankAccountNumber:"+account.getBankAccountNumber());
		System.out.println("option\n1-->withdraw\n2-->add money\nany key-->exit");
		sc.nextLine();
		String options=sc.nextLine();
		if(options.equals("1")) {
			withdraw(account);
		}
		else if(options.equals("2")) {
			BuyAndSell.addDematBalance(account);
		}
		
	}
	private static void withdraw(DematAccount account) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering withdraw method");  
		System.out.println("Balance Available:"+account.getGrow_More_Balance());
		while(true) {
		System.out.println("how much you want withdraw?");
		String amt;
		while(true) {
			amt=sc.nextLine();
			if(signInProcess.isNumeric(amt)) {
				break;
			}
			System.out.println("please enter valid input");
		}

		while(true) {
			String captcha=signInProcess.captchaGeneration();
			System.out.println("Check Captcha.."+captcha);
			String captchaCheck=sc.nextLine();
			if(captcha.equals(captchaCheck)) {
				break;
			}else {
				System.err.println("ReEnter Captcha...");
			}}
		if(account.getGrow_More_Balance()>=Double.parseDouble(amt)&&amt.length()>=3) {
			account.setGrow_More_Balance(account.getGrow_More_Balance()-Double.parseDouble(amt));
			Iterator itr=MainClass.bankDetails.iterator();
			BankDetails bank=null;
			while(itr.hasNext()) {
				bank=(BankDetails)itr.next();
				if(bank.BankAccountNumber.equals(account.getBankAccountNumber())) {
					break;
				}
			}
			bank.addBankBalance(Double.parseDouble(amt));
			try {
				PreparedStatement stmt=dbConnection.prepareStatement("update BankDetails set balance=? where BankAccountNumber=? ;");
				
				stmt.setString(1, ""+bank.Balance);
				stmt.setString(2, ""+bank.BankAccountNumber);
				stmt.execute();
				PreparedStatement stmt1=dbConnection.prepareStatement("update DematAccount set grow_more_balance=? where bankAccountNumber=? ;");
				
				stmt1.setString(1, ""+account.getGrow_More_Balance());
				stmt1.setString(2, ""+bank.BankAccountNumber);			
				stmt1.execute();
				System.out.println("Payment Successfully withdrawn");
				} catch (SQLException e) {
				// TODO Auto-generated catch block
					MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
						
				System.out.println(e.getMessage());
				
				}
			break;
			
		}
		else if(amt.length()<=2) {
			System.err.println("minimum withdrawable amount 100");
		}
		else {
			System.out.println("please enter valid input");
		}
		}
	}
	
	private static void Support() {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering support method");  
		helpAndSupport.helpMain();
	}
	public static JSONObject marketPriceCheck(String isin) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering marketPriceCheck method");  
		URL url=null;
		HttpURLConnection conn=null;
		InputStream is=null;
		try {
		switch(isin) {
			case "INE213A01029":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/ONGC");
				break;
			case "INE245A01021":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/TATAPOWER");
				break;
				
			case "INE467B01029":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/TCS");
				break;
				
			case "INE075A01022":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/WIPRO");
				break;
				
			case "INE758T01015":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/ZOMATO");
				break;
				
			case "INE154A01025":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/ITC");
				break;
			case "INE062A01020":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/SBIN");
				break;
				
			case "INE987B01026":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/NATCOPHARM");
				break;
				
			case "IN9155A01020":
				url=new URL("https://groww.in/v1/api/stocks_data/v1/accord_points/exchange/NSE/segment/CASH/latest_prices_ohlc/TATAMOTORS");
				break;
			
			default:
				break;
				
		}
		}catch(MalformedURLException e) {
			MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
		}

		if(url!=null) {

		try {
		
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
		
			MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e1);
		}
	
		try {

			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
		
			MainClass.log.log(Level.ERROR," "+LocalDateTime.now()+" Error Occured.", e);
		}
		
		try {
			
			conn.connect();
			
			
		
		} catch (Exception e1) {
		
			MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e1);
		}
		try {
		
			is = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", e);
		}
		Scanner s=new Scanner(is).useDelimiter("//A");
		String result = s.hasNext() ? s.next() : "";
//		System.out.println(result);
		Object obj=JSONValue.parse(result);
		JSONObject jsonObj=(JSONObject) obj;
		jsonObj.keySet();

		return (JSONObject) jsonObj;
	}
		else {
		return null;
		}

}
	public static String ISINNumber(String select) {
		MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering isis number method");  
		String isin="";
	
		switch(select) {
		case "1":
			isin="INE213A01029";
			break;
		case "2":
			isin="INE245A01021";
			break;
			
		case "3":
			isin="INE467B01029";
			break;
			
		case "4":
			isin="INE075A01022";
			break;
			
		case "5":
			isin="INE758T01015";
			break;
			
		case "6":
			isin="INE154A01025";
			break;
		case "7":
			isin="INE062A01020";
			break;
			
		case "8":
			isin="INE987B01026";
			break;
			
		case "9":
			isin="IN9155A01020";
			break;
		
		}
		return isin;
	}
	public static void getDBConnection() 
	{MainClass.log.log(Level.DEBUG," "+LocalDateTime.now()+" entering getDBConnection method");  
		if(dbConnection == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GrowMore","root","root"); 
//					System.out.println("1st query executed");
			}
			catch(Exception ex) {
				MainClass.log.log(Level.ERROR, " "+LocalDateTime.now()+" Error Occured.", ex);
			}
		}
	}
	
	
	
	}
