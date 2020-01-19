package application;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import data.ClientInfo;
import data.Controllers;
import data.Schedule;
import data.TimeLine;
import data.TimeLineCell;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class TimeLineController extends TimeLine implements Initializable {

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
    private int						currentDayOfWeek; // 현재 보고있는 요일
    
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
		super.isEditable = true;
		Controllers.timeLineController = this;

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
			if(data.ClientInfo.groupId.equals("null")) {
				try {
					Parent second;
					second = FXMLLoader.load(getClass().getResource("templates/groupGateway.fxml"));
					second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
					Scene sc = new Scene(second);
					Stage stage = (Stage)MoveToGroupButton.getScene().getWindow();
					stage.setScene(sc);
					stage.show();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}else {
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
				currentDayOfWeek = ClientInfo.today;
				showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
			}
		});
		///////////////////////////////////////////////////
		
		
		////////// 타임라인 일정 색상 표시 기능 //////////
		timeLineListView.setCellFactory(lv -> new TimeLineCell(this, selectedScheduleList));
		///////////////////////////////////////////
		
		
		////////// 서버로부터 스케줄 String을 받아 처리하는 부분 //////////
		currentDayOfWeek = ClientInfo.today;
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
		dayOfWeekLabel.setText(ClientInfo.dayOfWeekList[currentDayOfWeek] + "요일");
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
	
	@Override
	public void showSchedules(String dayOfWeek) {
		super.showSchedules(dayOfWeek);
		dayOfWeekLabel.setText(ClientInfo.dayOfWeekList[currentDayOfWeek] + "요일");
	}

	@FXML public void moveToLeft() {
		if(currentDayOfWeek > 0) {
			currentDayOfWeek -= 1;
		} else {
			currentDayOfWeek = ClientInfo.dayOfWeekList.length - 1;
		}
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
	}

	@FXML public void moveToRight() {
		if(currentDayOfWeek < ClientInfo.dayOfWeekList.length - 1) {
			currentDayOfWeek += 1;
		} else {
			currentDayOfWeek = 0;
		}
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
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
		Schedule.setToday(); // TODO 이렇게 하는 거 맞음??
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
