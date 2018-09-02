package me.boops.functions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class SaveTarFile {
	
	public void save(String name, String path) {
		
		try {
			
			FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + File.separator + name + ".tar.gz");
			GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
			TarArchiveOutputStream tos = new TarArchiveOutputStream(gos);
			
			File folder = new File(path);
			File[] files = folder.listFiles();
			
			for (int i = 0; i < files.length; i++) {
				
				TarArchiveEntry entry = new TarArchiveEntry(files[i], files[i].getAbsolutePath());
				entry.setName(files[i].getName());
				tos.putArchiveEntry(entry);
				
				FileInputStream fis = new FileInputStream(files[i]);
				BufferedInputStream bis = new BufferedInputStream(fis);
				IOUtils.copy(bis, tos);
				tos.closeArchiveEntry();
				fis.close();
				
			}
			
			tos.close();
			gos.close();
			fos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
