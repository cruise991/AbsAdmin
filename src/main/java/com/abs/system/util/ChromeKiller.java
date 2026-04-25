package com.abs.system.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ChromeKiller {

	public static void killChromeProcess() throws IOException {
		// 获取Chrome进程的PID
		ProcessBuilder processBuilder=new ProcessBuilder("tasklist");
		Process process1=processBuilder.start();
		String processList=new BufferedReader(new InputStreamReader(process1.getInputStream())).lines().collect(Collectors.joining("\n"));
		System.out.println(processList);
	
		String [] processes=processList.split("\n");
		for(String p:processes) {
		    p=p.toLowerCase();
			if(p.contains("chrome.exe") ) {
				ProcessBuilder kill=new ProcessBuilder("taskkill","/F","/IM","chrome.exe");
				kill.start();

			}
			
		}
		
		
		
		String command = "tasklist /FI \"IMAGENAME eq chrome.exe\" /FO CSV /NH";
		Process process = Runtime.getRuntime().exec(command);
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		String pid = null;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(",");
			if (parts.length == 2) {
				pid = parts[1].trim();
				break;
			}
		}
		reader.close();
        System.out.println("是否存在谷歌相关进程"+pid);
		// 如果找到了进程的PID，杀死该进程
		if (pid != null) {
			command = "taskkill /F /PID " + pid;
			Runtime.getRuntime().exec(command);
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		killChromeProcess();
	}

}
