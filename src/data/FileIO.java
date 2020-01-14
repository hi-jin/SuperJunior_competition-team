package data;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileIO { // TODO utf-8 인코딩

	private static final File user = new File("user.txt"); 
	
	public static void write() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(user));
			if(ClientInfo.userId != null && !ClientInfo.userId.equals("")) {
				out.println(ClientInfo.userId + "///" + ClientInfo.schedules);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) out.close();
		}
	}
	
	public static void read() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(user));
			String userStr = in.readLine();
			System.out.println(userStr);
			if(userStr != null && !userStr.equals("")) {
				String[] data = userStr.split("///");
				ClientInfo.userId = data[0];
				if(data.length == 2) ClientInfo.schedules = data[1];
				else ClientInfo.schedules = "";
			} else {
				user.delete();
				user.createNewFile();
				ClientInfo.userId = "";
				ClientInfo.schedules = "";
			}
		} catch (FileNotFoundException e) {
			try {
				user.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (EOFException e) {
			user.delete();
			try {
				user.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
