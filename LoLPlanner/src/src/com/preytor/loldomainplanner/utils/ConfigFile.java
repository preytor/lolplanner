package com.preytor.loldomainplanner.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

public class ConfigFile {
	

	public static void createConfigFile() throws IOException{
		
		File cache = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath().toString()+"/lolplanner/cache/");
		cache.getParentFile().mkdirs(); 
		File logs = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath().toString()+"/lolplanner/logs/");
		logs.getParentFile().mkdirs(); 
		File settings = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath().toString()+"/lolplanner/settings.properties");
		settings.getParentFile().mkdirs(); 
		settings.createNewFile();
		
		OutputStream output = new FileOutputStream(settings);
		Properties prop = new Properties();
		prop.setProperty("width", "800");
		prop.setProperty("height", "600");
		prop.setProperty("fullscreen", "false");
//		prop.setProperty("", "");
		
		prop.store(output, null);
		output.close();
		
		System.out.println("file created");
	}
}
