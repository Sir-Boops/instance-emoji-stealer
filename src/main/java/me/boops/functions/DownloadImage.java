package me.boops.functions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadImage {
	
	public void download(String name, String URL, String path) {
	
		try {
			
			// Might be a retry if it's already there skip it
			
			if(!new File(path + name + ".png").exists()) {
				
				URL url = new URL(URL);
				
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setReadTimeout(10 * 1000);
				conn.setConnectTimeout(10 * 1000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
				
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				FileOutputStream fos = new FileOutputStream(new File(path + name + ".png"));
				
				int inByte;
				while ((inByte = is.read()) != -1) {
					bos.write(inByte);
				}
				
				fos.write(bos.toByteArray());
				
				fos.close();
				is.close();
				bos.close();
				
				System.out.println("Got emoji " + name);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to get emoji " + name);
		}
	}
}
