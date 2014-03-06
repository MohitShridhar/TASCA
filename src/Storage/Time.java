package Storage;

public class Time {
private int year, month, day, hour, minute;
	
	public Time (int y, int m, int d, int h, int min){
		year = y;
		month = m;
		day = d;
		hour = h;
		minute = min;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setYear(int y) {
		year = y;
		return;
	}
	
	public void setMonth(int m) {
		month = m;
		return;
	}
	
	public void setDay(int d) {
		day = d;
		return;
	}
	
	public void setHour(int h) {
		hour = h;
		return;
	}
	
	public void setMinute(int min) {
		minute = min;
		return;
	}
	

}
