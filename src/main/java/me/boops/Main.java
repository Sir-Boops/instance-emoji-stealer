package me.boops;

import java.io.File;

import org.json.JSONArray;

import me.boops.functions.DownloadImage;
import me.boops.functions.FetchEmojiList;
import me.boops.functions.SaveTarFile;

public class Main {

	public static void main(String[] args) {
		
		// Bitch at the user
		if(args.length > 1 || args.length == 0) {
			System.out.println("You must provide a URL to steal emojis from!");
			System.exit(0);
		}
		
		// Assume the URL looks something like 'mastodon.sergal.org'
		
		// Load the emoji list
		JSONArray emoji_list_arr = new FetchEmojiList().load(args[0]);
		System.out.println("Found " + emoji_list_arr.length() + " emojis on " + args[0]);
		
		// get the folder name & path to save the emojis in
		String emoji_dir = System.getProperty("user.dir") + File.separator + args[0] + File.separator;
		
		// Create a folder to save the emojis in
		new File(emoji_dir).mkdirs();
		System.out.println("Saving emojis to " + emoji_dir);
		
		// Loop over and grab all the emojis
		for (int i = 0; i < emoji_list_arr.length(); i++) {
			new DownloadImage().download(emoji_list_arr.getJSONObject(i).getString("shortcode"), emoji_list_arr.getJSONObject(i).getString("url"), emoji_dir);
		}
		
		// Create the tar.gz file for import
		new SaveTarFile().save(args[0], emoji_dir);
	}
}
