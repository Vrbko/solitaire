package com.boardgame;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.boardgame.screen.config.GameConfig;

import java.awt.DisplayMode;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument

// DEPLOY ./gradlew desktop:dist
// shrani v \solitaire\core\build\libs
// mislim da nena overrida, zbris


// proble ce sploh dela, k neke fuka
//  ./gradlew desktop:run, before a ./gradlew desktop:dist


public class DesktopLauncher {
	public static void main (String[] arg) {
		System.out.println("Working Directory: " + System.getProperty("user.dir"));

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Spider Solitaire");
		config.setWindowedMode((int) 1920, (int) 990);


		config.setWindowIcon(Files.FileType.Internal,"spider2.png");
		new Lwjgl3Application(new BoardGame(), config);
	}
}
