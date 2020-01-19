package data;

import java.net.Socket;
import java.util.Vector;

public class ClientInfo {
	public static Socket socketInfo = null; //클라이언트 소켓 객체 저장
	public static boolean connection = false; 
	public static String userId = "";
	public static String groupId = "";
	public static String schedules = "";
	public static int finishedChallengesCount = 0;
	public static int allChallengesCount = 0;
	public static Vector<Schedule> scheduleList = new Vector<>();
	public static  String[] dayOfWeekList = {"일", "월", "화", "수", "목", "금", "토"};
	public static int today;
	
	public static void setSocket(Socket sk) {
		socketInfo = sk;
	}
}
