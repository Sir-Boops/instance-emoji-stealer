package me.boops.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;

public class FetchEmojiList {
	
	public JSONArray load(String domain) {
		
		JSONArray emoji_list = new JSONArray();
		
		try {
			
			URL url = new URL("https://" + domain + "/api/v1/custom_emojis");
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setInstanceFollowRedirects(true);
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setRequestMethod("GET");
			
			conn.connect();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			StringBuilder sb = new StringBuilder();
			String inByte;
			while ((inByte = in.readLine()) != null) {
				sb.append(inByte);
			}
			
			emoji_list = new JSONArray(sb.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return emoji_list;
	}
	
}
