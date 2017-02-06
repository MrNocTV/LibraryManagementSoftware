import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DDate {
	protected int day;
	protected int month;
	protected int year;
	
	//d1 always >= d2
	public static long distanceBetweenDate(DDate d1, DDate d2) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date day1 = dateFormat.parse(d1.toString());
		Date day2 = dateFormat.parse(d2.toString());
		long diff = day1.getTime()-day2.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	public static int compareDate(DDate d1, DDate d2) {
		//return 0 if d1 == d2
		//return 1 if d1 > d2
		//return -1 if d1 < d2
		int yearCompare = d1.year - d2.year;
		if(yearCompare != 0)
			return yearCompare;
		int monthCompare = d1.month - d2.month;
		if(monthCompare != 0)
			return monthCompare;
		return d1.day - d2.day;
		
	}
	
	public DDate(String sDay) {
		String[] splits = sDay.split("-");
		if(splits.length == 3) {
			year = new Integer(splits[0]);
			month = new Integer(splits[1]);
			day = new Integer(splits[2]);
		}
	}
	
	
	
	@Override
	public String toString() {
		return String.format("%04d-%02d-%02d", year, month, day);
	}
}
