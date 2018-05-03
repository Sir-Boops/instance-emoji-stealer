package me.boops.functions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import me.boops.Main;

public class CheckThread implements Runnable {

	private String domain;
	private int on_i;
	private int total;
	
    public CheckThread(String domain, int on_i, int total) {
       this.domain = domain;
       this.on_i = on_i;
       this.total = total;
    }
	
	@Override
	public void run() {
		
		System.out.println("Checking '" + domain + "' " + on_i + "/" + total);
		List<JSONObject> emoji_list = new ArrayList<JSONObject>();

		// Try to load the remote emojis
		try {
			emoji_list.addAll(new Arr2List().convert(new JSONArray(new FetchRemoteData().fetch(domain, "/api/v1/custom_emojis"))));
		} catch (Exception e) {
		}
		
		// Add then to the emoji list
		for(int i = 0; i < emoji_list.size(); i++) {
			if(Main.master_list.has(emoji_list.get(i).getString("shortcode"))) {
				// Append intance to emoji list
				JSONObject urls = new JSONObject();
				urls.put("url", emoji_list.get(i).getString("url"));
				urls.put("url_scaled", emoji_list.get(i).getString("static_url"));
				Main.master_list.getJSONArray(emoji_list.get(i).getString("shortcode")).put(new JSONObject().put("domain", domain).put("urls", urls));
			} else {
				// Add emoji to list then append the instance
				JSONObject urls = new JSONObject();
				urls.put("url", emoji_list.get(i).getString("url"));
				urls.put("url_scaled", emoji_list.get(i).getString("static_url"));
				Main.master_list.append(emoji_list.get(i).getString("shortcode"), new JSONObject().put("domain", domain).put("urls", urls));
			}
		}	
		
	}

}
