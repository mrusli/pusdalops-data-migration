package mil.pusdalops.k2.webui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class App_tw {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello World!!!");
		
		// exmple tw & thn
		String tw = "717.1434";
		String thn = "2020";
		
		// is tw 9 char long?
		if (tw.length()<9) {
			// append '0' in frong
			tw = '0'+tw;
		} else {
			
		}
		
		System.out.println(tw);

		// split
		String[] datetime = tw.split("\\.");
		// convert to list
		List<String> list = Arrays.asList(datetime);
		
		// get the monthdate and time as string values
		String monthDateStr = null;
		String timeStr = null;		
		try {
			monthDateStr = list.get(0);
			timeStr = list.get(1);			
		} catch (Exception e) {
			throw new Exception("Tw format: "+tw+" tidak sesuai.");
		}

		System.out.println(monthDateStr);
		System.out.println(timeStr);
		
		// split the month, day, hour and minutes
		String month = monthDateStr.substring(0, 2);
		String day = monthDateStr.substring(2, 4);		
		
		String hour = timeStr.substring(0, 2);
		String mint = timeStr.substring(2, 4);

		// construct a LocalDate object
		LocalDate localdate = null;
		try {
			localdate = LocalDate.of(Integer.valueOf(thn), Integer.valueOf(month), Integer.valueOf(day));			
		} catch (Exception e) {
			throw new Exception("Tanggal: "+
					thn+"-"+
					month+"-"+
					day+"-"+
					" tidak sesuai");
		}
		
		// construct a LocalTime object
		LocalTime localtime = null;
		try {
			localtime = LocalTime.of(Integer.valueOf(hour), Integer.valueOf(mint));			
		} catch (Exception e) {
			throw new Exception("Jam: "+
					hour+":"+
					mint+
					" tidak sesuai");			
		}		

		LocalDateTime localDateTime = LocalDateTime.of(localdate, localtime);
		System.out.println(localDateTime);
	}
}
