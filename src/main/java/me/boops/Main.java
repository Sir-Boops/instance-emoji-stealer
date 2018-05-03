package me.boops;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		if(args.length <= 0) {
			System.out.println("Define a starting instance!");
			System.exit(1);
		}
		
		System.out.println("Using '" + args[0] + "' as the starting point");
		
		List<String> found_instances = new ArrayList<String>();
		List<String> to_scan = new ArrayList<String>();
		List<String> scanned = new ArrayList<String>();
		List<String> emoji_list = new ArrayList<String>();
		List<String> emoji_url = new ArrayList<String>();
		JSONObject master_list = new JSONObject();
		
		// Get inital list
		to_scan.add(args[0]);
		to_scan.addAll(getInstanceList(args[0]));
		
		ThreadGroup scanGroup = new ThreadGroup("scanGroup");
		while(to_scan.size() > 0) {
			
			if(scanGroup.activeCount() < 20) {
				scanned.add(to_scan.get(0));
				new Thread(scanGroup, new Runnable() {
					String scan_url = to_scan.get(0);
					public void run() {
						
						String url_to_scan = scan_url;
						JSONArray instance_data = new JSONArray();
						
						List<String> new_found = new ArrayList<String>();
						JSONArray new_emoji = new JSONArray();
						System.out.println("Scanning '" + url_to_scan + "'");
						
						// Get more instances
						try {
							new_found.addAll(getInstanceList(url_to_scan));
						} catch (Exception e) {
							System.out.println("Error fetching data from '" + url_to_scan + "'");
						}
						
						// Load emojis
						try {
							new_emoji = getInstanceEmoji(url_to_scan);
						} catch (Exception e) {
							System.out.println("Error fetching data from '" + url_to_scan + "'");
						}
						
						// Add emojis
						for(int i = 0; i < new_emoji.length(); i++) {
							String emoji_name = new_emoji.getJSONObject(i).getString("shortcode");
							if(!listContains(emoji_list, emoji_name)) {
								emoji_list.add(emoji_name);
								emoji_url.add(new_emoji.getJSONObject(i).getString("url"));
							}
							JSONObject emoji = new JSONObject();
							emoji.append("code", emoji_name);
							JSONObject urls = new JSONObject();
							urls.append("og", new_emoji.getJSONObject(i).getString("url"));
							urls.append("static", new_emoji.getJSONObject(i).getString("static_url"));
							emoji.append("urls", urls);
							instance_data.put(emoji);
						}
						
						// Add newly found instances to the found_instances list
						for(int i = 0; i < new_found.size(); i++) {
							if(!listContains(found_instances, new_found.get(i))) {
								found_instances.add(new_found.get(i));
							}
							
							if(!listContains(scanned, new_found.get(i)) && !listContains(to_scan, new_found.get(i))) {
								to_scan.add(new_found.get(i));
							}
						}
						
						System.out.println("Amount of currently know instances: " + found_instances.size());
						master_list.put(url_to_scan, instance_data);
					}
				}).start();
				to_scan.remove(to_scan.get(0));
				System.out.println("Left to scan: " + to_scan.size());
				System.out.println("Current emoji count: " + emoji_list.size());
			}
		}
		
		while(scanGroup.activeCount() > 0) {
			Thread.sleep(10);
		}
		
		PrintWriter file = new PrintWriter(new FileWriter("instance_list"));
		for(int i = 0; i < found_instances.size(); i++) {
			file.println(found_instances.get(i));
		}
		file.close();
		
		PrintWriter file2 = new PrintWriter(new FileWriter("emoji_list"));
		for(int i = 0; i < emoji_list.size(); i++) {
			file2.println(emoji_list.get(i) + " -> " + emoji_url.get(i));
		}
		file2.close();
		
		PrintWriter file3 = new PrintWriter(new FileWriter("master_list.json"));
		file3.print(master_list);
		file3.close();
	}
	
	private static boolean listContains(List<String> toSearch, String term) {
		boolean ans = false;
		for(int i = 0;  i < toSearch.size(); i++) {
			if(toSearch.get(i).equalsIgnoreCase(term)) {
				ans = true;
			}
		}
		return ans;
	}
	
	private static List<String> getInstanceList(String URL) throws Exception {
		
		URL url = new URL("https://" + URL + "/api/v1/instance/peers");
		
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
		
		List<String> ans = new ArrayList<String>();
		JSONArray arr = new JSONArray(sb.toString());
		
		for(int i = 0; i < arr.length(); i++) {
			ans.add(arr.getString(i));
		}
		
		return ans;
	}
	
	private static JSONArray getInstanceEmoji(String URL) throws Exception {
		
		URL url = new URL("https://" + URL + "/api/v1/custom_emojis");
		
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
		
		return new JSONArray(sb.toString());
	}
}
