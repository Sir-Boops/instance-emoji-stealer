package me.boops;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONObject;

import me.boops.functions.CheckThread;
import me.boops.functions.ReadInitFile;

public class Main {
	
	public static JSONObject master_list = new JSONObject();

	public static void main(String[] args) throws Exception {

		List<String> instances = new ReadInitFile().read(args[0]);
		System.out.println("Found " + instances.size() + " URLs to check");

		ThreadGroup scanGroup = new ThreadGroup("scanGroup");
		for (int i = 0; i < instances.size(); i++) {

			// Wait for a thread
			while (scanGroup.activeCount() > Integer.parseInt(args[1])) {
				Thread.sleep(10);
			}

			// Start a new thread
			Thread thread = new Thread(scanGroup, new CheckThread(instances.get(i), i, instances.size()));
			thread.start();

		}

		while (scanGroup.activeCount() > 0) {
			System.out.println("Waiting for all threads to finish");
			Thread.sleep(1000);
		}
		
		PrintWriter file = new PrintWriter(new FileWriter("emoji_list.json"));
		file.print(master_list);
		file.close();
		
	}
}
