package data;

import java.util.Collections;
import java.util.Vector;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class TimeLineCell extends ListCell<String> {
	
	protected Vector<Schedule> selectedScheduleList = new Vector<>();
	protected ListView<String> timeLineListView;
	
	public TimeLineCell(ListView<String> timeLineListView, Vector<Schedule> selectedScheduleList) {
		super();
		this.timeLineListView = timeLineListView;
		this.selectedScheduleList = selectedScheduleList;
	}
	
	@Override
	protected void updateItem(String item, boolean empty) {
		String color = null;
		String otherColor;
		
		Collections.sort(selectedScheduleList);
		super.updateItem(item, empty);
		if(empty) {
			setText(null);
			setStyle("");
		} else {
			setText(item);
			setStyle("");
			for(int i = 0; i < selectedScheduleList.size(); i++) {
				Schedule schedule = selectedScheduleList.get(i);
				if(schedule.getType() == 'E') {
					color = "#A9F5F2";
				} else if(schedule.getType() == 'C') {
					color = "#ebedf0";
				} else if(schedule.getType() == 'D') {
					color = "#A9F5F2";
				}
				if(schedule.getStartIndex() < getIndex() && schedule.getEndIndex() > getIndex()) {
					setStyle("-fx-background-color: " + color);
					if(schedule.getType() == 'C') {
						this.setOnMouseClicked(event -> {
							if(event.getClickCount() == 2) {
								schedule.setType('D');
								ClientInfo.finishedChallengesCount++;
								timeLineListView.refresh();
							}
						});
					} else if(schedule.getType() == 'D' ) {
						this.setOnMouseClicked(event -> {
							if(event.getClickCount() == 2) {
								schedule.setType('C');
								ClientInfo.finishedChallengesCount--;
								timeLineListView.refresh();
							}
						});
					}
					break;
				} else if(schedule.getStartIndex() == getIndex()) {
					if(i > 0) {
						if(selectedScheduleList.get(i-1).getEndIndex() == getIndex()) {
							if(selectedScheduleList.get(i-1).getType() == 'E' || selectedScheduleList.get(i-1).getType() == 'D') otherColor = "#A9F5F2";
							else otherColor = "#ebedf0";
							setStyle("-fx-background-color: linear-gradient(" + otherColor + " 50%, " + color + " 50%)");
							break;
						} 
					}
					if(getIndex() % 2 == 0) {
						setStyle("-fx-background-color: linear-gradient(-fx-control-inner-background 50%, " + color + " 50%)");
					} else {
						setStyle("-fx-background-color: linear-gradient(derive(-fx-control-inner-background, -2%) 50%, " + color + " 50%)");
					}
				} else if(schedule.getEndIndex() == getIndex()) {
					if(i < selectedScheduleList.size()-1) {
						if(selectedScheduleList.get(i+1).getStartIndex() == getIndex()) {
							if(selectedScheduleList.get(i+1).getType() == 'E' || selectedScheduleList.get(i+1).getType() == 'D') otherColor = "#A9F5F2";
							else otherColor = "#ebedf0";
							setStyle("-fx-background-color: linear-gradient(" + color + " 50%, " + otherColor + " 50%)");
							break;
						} 
					}
					if(getIndex() % 2 == 0) {
						setStyle("-fx-background-color: linear-gradient(" + color + " 50%, -fx-control-inner-background 50%)");
					} else {
						setStyle("-fx-background-color: linear-gradient(" + color + " 50%, derive(-fx-control-inner-background, -2%) 50%)");
					}
				}
			}
		}
	}
}
