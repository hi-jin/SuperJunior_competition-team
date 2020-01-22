package data;

import java.util.Collections;
import java.util.Vector;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;

public class TimeLineCell extends ListCell<String> {
	
	protected Vector<Schedule> 	selectedScheduleList = new Vector<>();
	protected TimeLine 			timeLine;
	public TimeLineCell(TimeLine timeLine, Vector<Schedule> selectedScheduleList) {
		super();
		this.timeLine = timeLine;
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
					this.setOnMouseClicked(event -> {
						this.getOnMouseClicked();
						if(event.getButton() == MouseButton.SECONDARY) {
							String type = (schedule.getType() == 'E') ? "필수일정" : "도전일정";
							Alert alert = new Alert(AlertType.CONFIRMATION, type + " " + schedule.getTitle() + " 정말 삭제하시겠습니까?", ButtonType.NO, ButtonType.YES);
							alert.showAndWait();
							if(alert.getResult() == ButtonType.YES) {
								if(schedule.getType() == 'C') {
									ClientInfo.allChallengesCount--;
								}
								ClientInfo.scheduleList.remove(schedule);
								timeLine.eraseSchedule(schedule);
								timeLine.timeLineListView.refresh();
							}
						}
					});
					setStyle("-fx-background-color: " + color);
					if(schedule.getType() == 'C') {
						this.setOnMouseClicked(event -> {
							this.getOnMouseClicked();
							if(event.getClickCount() == 2) {
								schedule.setType('D');
								ClientInfo.finishedChallengesCount++;
								timeLine.showSchedules(schedule.dayOfWeek);
								timeLine.timeLineListView.refresh();
							}
							if(event.getButton() == MouseButton.SECONDARY) {
								String type = (schedule.getType() == 'E') ? "필수일정" : "도전일정";
								Alert alert = new Alert(AlertType.CONFIRMATION, type + " " + schedule.getTitle() + " 정말 삭제하시겠습니까?", ButtonType.NO, ButtonType.YES);
								alert.showAndWait();
								if(alert.getResult() == ButtonType.YES) {
									if(schedule.getType() == 'C') {
										ClientInfo.allChallengesCount--;
									}
									ClientInfo.scheduleList.remove(schedule);
									timeLine.eraseSchedule(schedule);
									timeLine.timeLineListView.refresh();
								}
							}
						});
					} else if(schedule.getType() == 'D') {
						this.setOnMouseClicked(event -> {
							this.getOnMouseClicked();
							if(event.getClickCount() == 2) {
								schedule.setType('C');
								ClientInfo.finishedChallengesCount--;
								timeLine.showSchedules(schedule.dayOfWeek);
								timeLine.timeLineListView.refresh();
							}
							if(event.getButton() == MouseButton.SECONDARY) {
								String type = (schedule.getType() == 'E') ? "필수일정" : "도전일정";
								Alert alert = new Alert(AlertType.CONFIRMATION, type + " " + schedule.getTitle() + " 정말 삭제하시겠습니까?", ButtonType.NO, ButtonType.YES);
								alert.showAndWait();
								if(alert.getResult() == ButtonType.YES) {
									if(schedule.getType() == 'C') {
										ClientInfo.allChallengesCount--;
									}
									ClientInfo.scheduleList.remove(schedule);
									timeLine.eraseSchedule(schedule);
									timeLine.timeLineListView.refresh();
								}
							}
						});
					}
					break;
				} else if(schedule.getStartIndex() == getIndex()) {
					this.setOnMouseClicked(event -> {
						this.getOnMouseClicked();
						if(event.getButton() == MouseButton.SECONDARY) {
							String type = (schedule.getType() == 'E') ? "필수일정" : "도전일정";
							Alert alert = new Alert(AlertType.CONFIRMATION, type + " " + schedule.getTitle() + " 정말 삭제하시겠습니까?", ButtonType.NO, ButtonType.YES);
							alert.showAndWait();
							if(alert.getResult() == ButtonType.YES) {
								if(schedule.getType() == 'C') {
									ClientInfo.allChallengesCount--;
								}
								ClientInfo.scheduleList.remove(schedule);
								timeLine.eraseSchedule(schedule);
								timeLine.timeLineListView.refresh();
							}
						}
					});
					if(schedule.getType() == 'C') {
						this.setOnMouseClicked(event -> {
							this.getOnMouseClicked();
							if(event.getClickCount() == 2) {
								schedule.setType('D');
								ClientInfo.finishedChallengesCount++;
								timeLine.showSchedules(schedule.dayOfWeek);
								timeLine.timeLineListView.refresh();
							}
							if(event.getButton() == MouseButton.SECONDARY) {
								String type = (schedule.getType() == 'E') ? "필수일정" : "도전일정";
								Alert alert = new Alert(AlertType.CONFIRMATION, type + " " + schedule.getTitle() + " 정말 삭제하시겠습니까?", ButtonType.NO, ButtonType.YES);
								alert.showAndWait();
								if(alert.getResult() == ButtonType.YES) {
									if(schedule.getType() == 'C') {
										ClientInfo.allChallengesCount--;
									}
									ClientInfo.scheduleList.remove(schedule);
									timeLine.eraseSchedule(schedule);
									timeLine.timeLineListView.refresh();
								}
							}
						});
					} else if(schedule.getType() == 'D') {
						this.setOnMouseClicked(event -> {
							this.getOnMouseClicked();
							if(event.getClickCount() == 2) {
								schedule.setType('C');
								ClientInfo.finishedChallengesCount--;
								timeLine.showSchedules(schedule.dayOfWeek);
								timeLine.timeLineListView.refresh();
							}
							if(event.getButton() == MouseButton.SECONDARY) {
								String type = (schedule.getType() == 'E') ? "필수일정" : "도전일정";
								Alert alert = new Alert(AlertType.CONFIRMATION, type + " " + schedule.getTitle() + " 정말 삭제하시겠습니까?", ButtonType.NO, ButtonType.YES);
								alert.showAndWait();
								if(alert.getResult() == ButtonType.YES) {
									if(schedule.getType() == 'C') {
										ClientInfo.allChallengesCount--;
									}
									ClientInfo.scheduleList.remove(schedule);
									timeLine.eraseSchedule(schedule);
									timeLine.timeLineListView.refresh();
								}
							}
						});
					}
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
			if(!this.timeLine.isEditable) this.setOnMouseClicked(null);
		}
	}
}
