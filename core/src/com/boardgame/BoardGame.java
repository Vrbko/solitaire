package com.boardgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.screen.GameOverScreen;
import com.boardgame.screen.GameScreen;
import com.boardgame.screen.IntroScreen;
import com.boardgame.screen.LeaderboardScreen;
import com.boardgame.screen.MenuScreen;
import com.boardgame.screen.SettingsScreen;
import com.boardgame.screen.config.GameManager;

import java.awt.Menu;

public class BoardGame extends Game {
	private AssetManager assetManager;
	private SpriteBatch batch;

	Texture img;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);

		batch = new SpriteBatch();
		assetManager.load(AssetDescriptors.UI_FONT);
		assetManager.load(AssetDescriptors.UI_SKIN);

		assetManager.load(AssetDescriptors.GAMEPLAY2);
		assetManager.finishLoading();   // blocks until all assets are loaded
		//setScreen(new GameOverScreen(this));





			setScreen(new SettingsScreen(this));

	}


	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		assetManager.dispose();
	}
	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}


}