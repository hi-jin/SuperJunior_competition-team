package data;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Schedule implements Comparable<Schedule> {

	String dayOfWeek;
	String title;
	int startTime;
	int endTime;
	char type;
	
	int startIndex;
	int endIndex;
	
	public Schedule(String dayOfWeek, String title, String startTime, String endTime, char type) {
		this.dayOfWeek = dayOfWeek;
		this.title = title;
		this.startTime = Integer.parseInt(startTime);
		this.endTime = Integer.parseInt(endTime);
		this.type = type;
		
		int hour = this.startTime / 100;
		int min = this.startTime - hour * 100;
		startIndex = min / 10 + hour * 6 + 2;
		
		hour = this.endTime / 100;
		min = this.endTime - hour * 100;
		endIndex = min / 10 + hour * 6 + 2;
	}
	
	public void addSchedule(Schedule schedule) {
		ClientInfo.scheduleList.add(schedule);
	}
	
	public void removeSchedule(Schedule schedule) {
		ClientInfo.scheduleList.remove(schedule);
	}
	
	public static void readSchedule() {
		setToday();
		String line = ClientInfo.schedules;
		
		if(!line.equals("")) {
			String[] strScheduleList = line.split("//");
			for(int i = 0; i < strScheduleList.length; i++) {
				String[] command = strScheduleList[i].split("/");
				Schedule schedule = new Schedule(command[0], command[1], command[2], command[3], command[4].charAt(0));
				ClientInfo.scheduleList.add(schedule);
			}
		}
	}
	
	public static void writeSchedule() {
		Collections.sort(ClientInfo.scheduleList);
		String result = "";
		for(int i = 0; i < ClientInfo.scheduleList.size(); i++) {
			Schedule schedule = ClientInfo.scheduleList.get(i);
			result += schedule.getDayOfWeek() + "/";
			result += schedule.getTitle() + "/";
			result += schedule.getStartTime() + "/";
			result += schedule.getEndTime() + "/";
			result += schedule.getType() + "//";
		}
		ClientInfo.schedules = result;
	}
	
	public static void setToday() {
		ClientInfo.today = parseDayOfWeek((new SimpleDateFormat("E", Locale.KOREA).format(new Date())));
	}
	
	private static int parseDayOfWeek(String dayOfWeek) {
		int intDayOfWeek = -1;
		switch(dayOfWeek) {
		case "일":
			intDayOfWeek = 0;
			break;
		case "월":
			intDayOfWeek = 1;
			break;
		case "화":
			intDayOfWeek = 2;
			break;
		case "수":
			intDayOfWeek = 3;
			break;
		case "목":
			intDayOfWeek = 4;
			break;
		case "금":
			intDayOfWeek = 5;
			break;
		case "토":
			intDayOfWeek = 6;
			break;
		}
		return intDayOfWeek;
	}
	
	@Override
	public int compareTo(Schedule o) {
		if(startIndex > o.startIndex) return 1;
		else if(startIndex < o.startIndex) return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		Schedule s = (Schedule) o;
		if(this.title.equals(s.title) && this.startTime == s.startTime && this.endTime == s.endTime
				&& this.type == s.type && this.dayOfWeek == s.dayOfWeek)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return this.dayOfWeek + " " + this.title;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public char getType() {
		return type;
	}
	
	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}
	
	public String getDayOfWeek() {
		return this.dayOfWeek;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setType(char type) {
		this.type = type;
	}
}
