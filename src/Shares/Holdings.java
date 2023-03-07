package Shares;

import users.DematAccount;

public class Holdings {
	Long dematAccountNumber;

	String shareName;
	Object sharePrice;
	Object shareAvgPrice;
	Integer shareQuantity;
	String profitAndLoss;
	Double investedAmount;
	Double currentAmount;
	String ISIN;
	public Holdings(Long dematAccountNumber, String shareName, Object sharePrice, Object shareAvgPrice,
			Integer shareQuantity, String profitAndLoss, Double investedAmount, Double currentAmount, String iSIN) {
//		super();
		this.dematAccountNumber = dematAccountNumber;
		this.shareName = shareName;
		this.sharePrice = sharePrice;
		this.shareAvgPrice = shareAvgPrice;
		this.shareQuantity = shareQuantity;
		this.profitAndLoss = profitAndLoss;
		this.investedAmount= investedAmount;
		this.currentAmount = currentAmount;
		this.ISIN = iSIN;
	}
	

	public Long getDematAccountNumber() {
		return dematAccountNumber;
	}


	public void setDematAccountNumber(Long dematAccountNumber) {
		this.dematAccountNumber = dematAccountNumber;
	}
	public String getShareName() {
		return shareName;
	}
	public void setShareName(String shareName) {
		this.shareName = shareName;
	}
	public Object getSharePrice() {
		return sharePrice;
	}
	public void setSharePrice(Object sharePrice) {
		this.sharePrice = sharePrice;
	}
	public Object getShareAvgPrice() {
		return shareAvgPrice;
	}
	public void setShareAvgPrice(Object shareAvgPrice) {
		this.shareAvgPrice = shareAvgPrice;
	}
	public Integer getShareQuantity() {
		return shareQuantity;
	}
	public void setShareQuantity(Integer shareQuantity) {
		this.shareQuantity = shareQuantity;
	}
	public String getProfitAndLoss() {
		return profitAndLoss;
	}
	public void setProfitAndLoss(String profitAndLoss) {
		this.profitAndLoss = profitAndLoss;
	}
	public Double getInvestedAmount() {
		return investedAmount;
	}
	public void setInvestedAmount(Double investedAmount) {
		this.investedAmount = investedAmount;
	}
	public Double getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Double currentAmount) {
		this.currentAmount = currentAmount;
	}
	public String getISIN() {
		return ISIN;
	}
	public void setISIN(String iSIN) {
		this.ISIN = iSIN;
	}


}
