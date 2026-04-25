package com.abs.system.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AddressUtils {

	public static String getAddresses(String ip) {
		// 这里调用淘宝API
		String urlStr = "https://whois.pconline.com.cn/ip.jsp?ip=" + ip;

		try {
			Document doc = Jsoup.connect(urlStr).get();
			Elements ele = doc.getElementsByTag("body");
			return ele.get(0).text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
