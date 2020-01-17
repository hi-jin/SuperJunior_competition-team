package application;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import data.ClientInfo;
import data.Schedule;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class TimeLineController implements Initializable {

	@FXML ListView<String> timeLineListView;
	@FXML Button timeSettingButton;
	@FXML Label currentTimeLabel;
	@FXML Line currentTimeLine;
	@FXML Label dayOfWeekLabel;
	@FXML Button leftButton; // "<"
	@FXML Button rightButton; // ">"
	@FXML Button AddEssentialsButton;
	@FXML Button ShowTimeLineButton;
	@FXML Button AddChallengesButton;
	@FXML Button MoveToGroupButton;
	
	//////////////////////////////////////////////////////
	
	@FXML static Label 		sun, mon, tue, wed, thu, fri, sat;
	@FXML Label		nowMonth; // nowDate
	@FXML HBox		sunToMon;
	@FXML HBox 		month;
	@FXML static VBox 		week1, week2, week3, week4, week5;
	
	private double 					cellHeight = 24;
	// all(list의 길이) : speed = barLength(=1) : x(barSpeed) => x = speed * barLength / all
    private double 					speed = ((144.0*cellHeight) / (24 * 60 * 60.0)) * 1.0 / (144.0*cellHeight);
    private boolean 				timerFlag = true; // 애니메이션 실행 플래그
    private boolean 				timeSetting = true; // 타임라인 고정 플래그
    private double 					currentScroll; // 현재 스크롤의 위치
    private String[]				dayOfWeekList = {"일", "월", "화", "수", "목", "금", "토"};
    private int						currentDayOfWeek; // 현재 보고있는 요일
	private ObservableList<String> 	timeLineList;
    private Vector<Schedule> 		scheduleIndexList = new Vector<>();
    
    //////////////////////////////// 진척도 관련 필드 ///////////////////////////////////
	
	private final static int 	MAX_DAY_COUNT = 7;
	private final static int 	MAX_WEEK_COUNT = 5;
	
    private static Calendar calendar = Calendar.getInstance(); 						// 달력 객체
	
	private static HashMap<Integer, String> colorMap = new HashMap<>();				// 진척도에 따른 색깔이 들어간 Map
	
	private static Label[] week = new Label[]{ sun, mon, tue, wed, thu, fri, sat };	// 일주일과 관련된 요일이 들어간 Label 배열
	private static int[] 	weekColors = new int[] { 5, 5, 5, 5, 5, 5, 5 }; 				// 일주일과 관련된 색깔이 들어간 배열
	
	private static VBox[] 	weeks = new VBox[] { week1, week2, week3, week4, week5 }; 	// 몇주인지가 들어간 VBox배열
	private static int[][] 	weeksColors = new int[MAX_WEEK_COUNT][MAX_DAY_COUNT];
	
	private static int 		dayCount, weekCount; 									// 일주일중 몇일이 지났는지, 몇주가 지났는지를 카운트하는 정수
	private static boolean 	monthCheck;

	
    // 시간에 따라서 자동으로 스크롤 하는 애니메이션
    private AnimationTimer timer = new AnimationTimer() {
        private long lastUpdate = -1;
        private ScrollBar scrollbar;
        
        @Override
        public void start() {
            scrollbar = getVerticalScrollBar();
            super.start();
        }

        @Override
        public void handle(long now) {
        	if (lastUpdate < 0) {
            	lastUpdate = now;
                return;
            }

            ////////// "속도 * 시간 == 거리"를 통해 스크롤을 얼마나 이동할지 계산 //////////
            long elapsedNanos = now - lastUpdate;
            double delta = speed * elapsedNanos / 1_000_000_000;
            currentScroll += delta;
            ////////////////////////////////////////////////////////////////////

            
            ////////// 타임라인 고정 버튼이 켜져있는지 꺼져있는지에 따라 라벨 변경 //////////
            if(timerFlag) {
            	currentTimeLabel.setText("now");
            	currentTimeLine.setVisible(true);
            	scrollbar.setValue(currentScroll);
            } else {
            	currentTimeLabel.setText("");
            	currentTimeLine.setVisible(false);
            }
            ///////////////////////////////////////////////////////////////////
            
            lastUpdate = now;
        }
    };

    // listView의 scrollBar를 반환하는 메소드 (animation에 쓰기 위해)
    private ScrollBar getVerticalScrollBar() {
        ScrollBar scrollBar = null;
        for (Node node : timeLineListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation() == Orientation.VERTICAL) {
                    break;
                }
            }
        }
        return scrollBar;
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AddEssentialsButton.setOnMouseClicked(event -> {
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/first.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)AddEssentialsButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		ShowTimeLineButton.setOnMouseClicked(event -> {
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/TimeLine.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)ShowTimeLineButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		AddChallengesButton.setOnMouseClicked(event -> {
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/AddingChallenges.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)AddChallengesButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		MoveToGroupButton.setOnMouseClicked(event -> {
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/groupMain.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)MoveToGroupButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		
		////////// 각종 초기화 + 애니메이션 (타임라인) 시작 //////////
		if(data.Controllers.timeLineController == null) {
			data.Controllers.timeLineController = this;
			
			colorMap.put(0, "-fx-background-color: #ebedf0");
			colorMap.put(1, "-fx-background-color: #c6e48b");
			colorMap.put(2, "-fx-background-color: #7bc96f");
			colorMap.put(3, "-fx-background-color: #239a3b");
			colorMap.put(4, "-fx-background-color: #196127");
			colorMap.put(5, "-fx-opacity: 0");
			colorMap.put(6, "-fx-background-color: #ebedf0");
			colorMap.put(7, "-fx-background-color: #eba698");
			colorMap.put(8, "-fx-background-color: #cc614b");
			colorMap.put(9, "-fx-background-color: #962c17");
			colorMap.put(10, "-fx-background-color: #5e1405");
			
			dayCount = calendar.get(Calendar.DAY_OF_WEEK)-1;
			weekCount = 0;
		}
		
		nowMonth.setText((calendar.get(Calendar.MONTH)+1)+"월 ");
		
		for(int i=0; i<MAX_DAY_COUNT; i++) {
			Label days = (Label)sunToMon.getChildren().get(i);
			week[i] = days;
			week[i].setStyle(colorMap.get(weekColors[i]));
		}
		for(int i=0; i<MAX_WEEK_COUNT; i++) {
			VBox week = (VBox)month.getChildren().get(i);
			weeks[i] = week;
		}
		
		for(int i=0; i<weekCount; i++) {
			for(int j=0; j < MAX_DAY_COUNT; j++) {
				Label label = (Label) weeks[i].getChildren().get(j);
				label.setStyle(colorMap.get(weeksColors[i][j]));
			}
		}
		
		timerFlag = true;
		timeSetting = true;
		leftButton.setDisable(true);
		rightButton.setDisable(true);
		
		timeLineListView.setFixedCellSize(cellHeight);
		timeLineList = FXCollections.observableArrayList();
		timeLineListView.setItems(timeLineList);

		Platform.runLater(() -> timer.start());
		/////////////////////////////////////////////////////
		
		
		////////// 현재 시각에 알맞은 위치에 스크롤을 향하도록 함 //////////
		Date currentDate = new Date();
		Date start = null, end = null;
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		String day = transFormat.format(currentDate).split(" ")[0];
		try {
			start = transFormat.parse(day + " 00:00:00");
			end = transFormat.parse(day + " 24:00:00");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		this.currentScroll = 1.0 * (currentDate.getTime() - start.getTime()) / (end.getTime() - start.getTime());
		/////////////////////////////////////////////////////////
		
		
		////////// 버튼 클릭시 타임라인 고정 및 해제 가능 //////////
		timeSettingButton.setOnMouseClicked(event -> {
			if(timeSetting) {
				timerFlag = false;
				timeSetting = false;
				leftButton.setDisable(false);
				rightButton.setDisable(false);
				timeSettingButton.setText("타임라인 고정");
			} else {
				timerFlag = true;
				timeSetting = true;
				leftButton.setDisable(true);
				rightButton.setDisable(true);
				timeSettingButton.setText("고정 해제");
				currentDayOfWeek = parseDayOfWeek((new SimpleDateFormat("E", Locale.KOREA).format(new Date())));
				showSchedules(dayOfWeekList[currentDayOfWeek]);
			}
		});
		///////////////////////////////////////////////////
		
		
		////////// 타임라인 일정 색상 표시 기능 //////////
		timeLineListView.setCellFactory(lv -> new ListCell<String>() { // TODO 최적화
			@Override
			protected void updateItem(String item, boolean empty) {
				String color;
				String otherColor;
				
				Collections.sort(scheduleIndexList);
				super.updateItem(item, empty);
				if(empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					setStyle("");
					for(int i = 0; i < scheduleIndexList.size(); i++) {
						if(scheduleIndexList.get(i).getType() == 'E') {
							color = "#A9F5F2";
						} else {
							color = "#ebedf0";
						}
						if(scheduleIndexList.get(i).getStartIndex() < getIndex() && scheduleIndexList.get(i).getEndIndex() > getIndex()) {
							setStyle("-fx-background-color: " + color);
							break;
						} else if(scheduleIndexList.get(i).getStartIndex() == getIndex()) {
							if(i > 0) {
								if(scheduleIndexList.get(i-1).getEndIndex() == getIndex()) {
									if(scheduleIndexList.get(i-1).getType() == 'E') otherColor = "#A9F5F2";
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
						} else if(scheduleIndexList.get(i).getEndIndex() == getIndex()) {
							if(i < scheduleIndexList.size()-1) {
								if(scheduleIndexList.get(i+1).getStartIndex() == getIndex()) {
									if(scheduleIndexList.get(i+1).getType() == 'E') otherColor = "#A9F5F2";
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
		});
		///////////////////////////////////////////
		
		
		////////// 서버로부터 스케줄 String을 받아 처리하는 부분 //////////
		currentDayOfWeek = parseDayOfWeek((new SimpleDateFormat("E", Locale.KOREA).format(new Date())));
		showSchedules(dayOfWeekList[currentDayOfWeek]);
		dayOfWeekLabel.setText(dayOfWeekList[currentDayOfWeek] + "요일");
		//////////////////////////////////////////////////////////
		
		////////////////////// 진척도 표시 부분 //////////////////////
		
		/*
		nowDate.setText(calendar.get(Calendar.YEAR)+"년 "
				+(calendar.get(Calendar.MONTH)+1)+"월 "
				+calendar.get(Calendar.DATE)+"일 "
				+dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]);
		*/
		
		
		/////////////////////////////////////////////////////////
	}
	
	public int parseDayOfWeek(String dayOfWeek) {
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
	
	// 요일에 맞는 타임라인을 표시함
	public void showSchedules(String dayOfWeek) {
		clearSchedules();
		String line = ClientInfo.schedules;
		
		dayOfWeekLabel.setText(dayOfWeekList[currentDayOfWeek] + "요일");
		String[] scheduleList = line.split("//");
		for(int i = 0; i < scheduleList.length; i++) {
			String[] command = scheduleList[i].split("/");
			
			if(command[0].equals(dayOfWeek)) {
				writeSchedule(command[1], command[2], command[3], command[4]);
			}
		}
	}
	
	// 타임라인 초기화
	private void clearSchedules() {
		timeLineList.clear();
		scheduleIndexList.clear();
		
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
	private void writeSchedule(String title, String startTime, String endTime, String type) {
		Schedule schedule = new Schedule(startTime, endTime, type.charAt(0));
		
		timeLineList.set(schedule.getStartIndex(), getTime(schedule.getStartTime()) + " " + title);
		timeLineList.set(schedule.getEndIndex(), getTime(schedule.getEndTime()));
		
		scheduleIndexList.add(schedule);
		
		for(int i = schedule.getStartIndex() + 1; i < schedule.getEndIndex(); i++) {
			timeLineList.set(i, "");
		}
	}
	
	private String getTime(int time) {
		int hour = time / 100;
		int min = time - hour * 100;
		
		return String.format("%02d:%02d", hour, min);
	}
	
	private int addTime(int time1, int time2) {
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

	@FXML public void moveToLeft() {
		if(currentDayOfWeek > 0) {
			currentDayOfWeek -= 1;
		} else {
			currentDayOfWeek = dayOfWeekList.length - 1;
		}
		showSchedules(dayOfWeekList[currentDayOfWeek]);
	}

	@FXML public void moveToRight() {
		if(currentDayOfWeek < dayOfWeekList.length - 1) {
			currentDayOfWeek += 1;
		} else {
			currentDayOfWeek = 0;
		}
		showSchedules(dayOfWeekList[currentDayOfWeek]);
	}
	
	@FXML
	public void nextDay() {
		int color;
		if(calendar.get(Calendar.MONTH)%2 == 0) {
			color = (int)(Math.random()*5);
		} else {
			color = (int) (Math.random() * 5) + 6;
		}
		
		week[dayCount].setStyle(colorMap.get(color));
		
		weekColors[dayCount] = color;
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		dayCount++;
		
		/*
		nowDate.setText(calendar.get(Calendar.YEAR)+"년 "
				+(calendar.get(Calendar.MONTH)+1)+"월 "
				+calendar.get(Calendar.DATE)+"일 "
				+dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]);
		*/
		
		monthCheck = monthChecker();
		
		if(monthCheck) {
			changeColor();
			for(int i=0; i < weekCount; i++) {
				for(int j=0; j < MAX_DAY_COUNT; j++) {
					Label label = (Label) weeks[i].getChildren().get(j);
					label.setStyle(colorMap.get(0));
				}
			}
			
			Platform.runLater(() -> nowMonth.setText((calendar.get(Calendar.MONTH)+1)+"월 "));
			weekCount = 0;
		}
		
		if(dayCount > 6) {
			changeColor();
			for(Label label: week) {
				label.setStyle(colorMap.get(0));
			}
			weekColors = new int[] { 0, 0, 0, 0, 0, 0, 0 };
			dayCount = 0;
		}
		
		if(weekCount > 4) {
			for(int i=0; i < weekCount; i++) {
				for(int j=0; j < MAX_DAY_COUNT; j++) {
					Label label = (Label) weeks[i].getChildren().get(j);
					label.setStyle(colorMap.get(0));
				}
			}
			weekCount = 0;
		}
		
	}
	
	private void changeColor() {
		for(int i=0; i < MAX_DAY_COUNT; i++) {
			Label label = (Label) weeks[weekCount].getChildren().get(i);
			label.setStyle(colorMap.get(weekColors[i]));
			weeksColors[weekCount][i] = weekColors[i];
		}
		weekCount++;
	}
	
	private boolean monthChecker() {
		int todayDate = calendar.get(Calendar.DATE);
		
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-1);
		int pastDate = calendar.get(Calendar.DATE);
		
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		
		if(pastDate > todayDate) {
			return true;
		}
		return false;
	}
	
}
