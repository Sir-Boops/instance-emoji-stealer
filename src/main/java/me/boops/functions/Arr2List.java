package me.boops.functions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Arr2List {
	
	public List<JSONObject> convert (JSONArray arr){
		
		List<JSONObject> ans = new ArrayList<JSONObject>();
		
		for(int i = 0; i < arr.length(); i++) {
			ans.add(arr.getJSONObject(i));
		}
		
		return ans;
	}
}
