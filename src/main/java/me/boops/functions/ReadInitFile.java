package me.boops.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReadInitFile {

	public List<String> read(String path) {

		List<String> ans = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				ans.add(line);
			}
			br.close();
		} catch (Exception e) {
		}
		
		return ans;
		
	}
}
