package data;

import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TimeLine {

	protected static double 			cellHeight = 24;
	protected ObservableList<String> 	timeLineList = FXCollections.observableArrayList();
    protected Vector<Schedule> 			selectedScheduleList = new Vector<>();
    
	// 요일에 맞는 타임라인을 표시함
	public void showSchedules(String dayOfWeek) {
		clearSchedules();
		
		for(int i = 0; i < ClientInfo.scheduleList.size(); i++) {
			if(ClientInfo.scheduleList.get(i).getDayOfWeek().equals(dayOfWeek))
				drawSchedule(ClientInfo.scheduleList.get(i));
		}
	}
	
	// 타임라인 초기화
	public void clearSchedules() {
		timeLineList.clear();
		selectedScheduleList.clear();
		
		int time = 0000;
		
		timeLineList.add("");
		timeLineList.add("");
		
		for(int i = 0; i < 144; i++) {
			if(time % 100 == 0 || time % 100 == 30) {
				timeLineList.add(getTime(time));
			} else {
				timeLineList.add("");
			}
			time = addTime(time, 10);
		}
		for(int i = 0; i < 11; i++) {
			timeLineList.add("");
		}
	}
	
	// 타임라인에 스케줄 추가
	public void drawSchedule(Schedule schedule) {
		timeLineList.set(schedule.getStartIndex(), getTime(schedule.getStartTime()) + " " + schedule.getTitle());
		timeLineList.set(schedule.getEndIndex(), getTime(schedule.getEndTime()));
		
		selectedScheduleList.add(schedule);
		
		for(int i = schedule.getStartIndex() + 1; i < schedule.getEndIndex(); i++) {
			timeLineList.set(i, "");
		}
	}
	
	public static String getTime(int time) {
		int hour = time / 100;
		int min = time - hour * 100;
		
		return String.format("%02d:%02d", hour, min);
	}
	
	public static int addTime(int time1, int time2) {
		int result = 0;
		
		if(time2 > 0) {
			int h1 = time1 / 100;
			int m1 = time1 % 100;
			int h2 = time2 / 100;
			int m2 = time2 % 100;
			
			while(m1 + m2 >= 60) {
				m1 -= 60;
				h1 += 1;
			}
			result = (h1 + h2) * 100 + m1 + m2;
		} else if(time2 < 0){
			time2 *= -1;
			int h1 = time1 / 100;
			int m1 = time1 % 100;
			int h2 = time2 / 100;
			int m2 = time2 % 100;

			while(m1 - m2 < 0) {
				m1 += 60;
				h1 -= 1;
			}
			result = (h1 - h2) * 100 + m1 - m2;
		} else {
			result = time1;
		}
		
		return result;
	}
}
