package data;

import java.net.Socket;

public class ClientInfo {
	public static Socket socketInfo = null; //클라이언트 소켓 객체 저장
	public static boolean connection = false; 
	public static String userId = "";
	public static String groupId = "";
	public static String schedules = "";
	
	public static void setSocket(Socket sk){
		socketInfo = sk;
	}
}
