package com.boardgame;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.boardgame.screen.config.GameConfig;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(144);
		config.setTitle("Spider Solitaire");
		config.setWindowedMode((int) GameConfig.WIDTH, (int) GameConfig.HEIGHT);
		config.setWindowIcon(Files.FileType.Internal,"spider2.png");
		new Lwjgl3Application(new BoardGame(), config);
	}
}
