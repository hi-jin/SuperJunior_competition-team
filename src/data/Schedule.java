package data;

public class Schedule implements Comparable<Schedule> {

	int startTime;
	int endTime;
	char type;
	
	int startIndex;
	int endIndex;
	
	public Schedule(String startTime, String endTime, char type) {
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
	
	@Override
	public int compareTo(Schedule o) {
		if(startIndex > o.startIndex) return 1;
		else if(startIndex < o.startIndex) return -1;
		return 0;
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
}
