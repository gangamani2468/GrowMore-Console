package Shares;

public class ShareInfo {


	String shareName;
	Object sharePrice; 
	Object todayLow;
	Object todayHigh;
	
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
	public Object getTodayLow() {
		return todayLow;
	}
	public void setTodayLow(Object todayLow) {
		this.todayLow = todayLow;
	}
	public Object getTodayHigh() {
		return todayHigh;
	}
	public void setTodayHigh(Object todayHigh) {
		this.todayHigh = todayHigh;
	}
	
	public ShareInfo(String shareName, Object sharePrice, Object todayLow, Object todayHigh) {
//		super();
		this.shareName = shareName;
		this.sharePrice = sharePrice;
		this.todayLow = todayLow;
		this.todayHigh = todayHigh;
	}
	
	
}
