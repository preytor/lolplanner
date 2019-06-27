package com.preytor.loldomainplanner.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.preytor.loldomainplanner.Main;
import com.preytor.loldomainplanner.utils.ConfigFile;

public class DesktopLauncher {
	@SuppressWarnings("unused")
	public static void main (String[] arg) throws IOException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		final String VERSION = "0.5";
		
		config.allowSoftwareMode = true;
		
		config.resizable = true;

		File file = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath().toString()+"/lolplanner");
		if(!file.exists() && !file.isDirectory()){
			System.out.println("doesnt exist");
			ConfigFile.createConfigFile();
			config.width = 800;
			config.height = 600;
			config.fullscreen = false;
		}else{
			System.out.println("already exist");
			Properties prop = new Properties();
			InputStream input = new FileInputStream(FileSystemView.getFileSystemView().getDefaultDirectory().getPath().toString()+"/lolplanner/settings.properties");
			if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                ConfigFile.createConfigFile();
                return;
            }
			
			prop.load(input);
			
			
			config.width = Integer.parseInt(prop.getProperty("width"));
			config.height = Integer.parseInt(prop.getProperty("height"));
			config.fullscreen = Boolean.parseBoolean(prop.getProperty("fullscreen"));
		}
		
		

		
		config.title = "Lands of Lords Domain Planner v"+VERSION;

		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		
		config.forceExit = true;
		config.addIcon("textures/ui/mainmenu/romanicon.jpg", FileType.Internal);
		


		
		new LwjglApplication(new Main(), config);
	}
}
