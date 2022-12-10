package com.boardgame.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.config.GameConfig;
import com.boardgame.screen.config.GameManager;


public class MenuScreen extends ScreenAdapter {

    private final BoardGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas2;

    public MenuScreen(BoardGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);

        gameplayAtlas2 = assetManager.get(AssetDescriptors.GAMEPLAY);
        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(20);

        TextureRegion backgroundRegion = gameplayAtlas2.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));



         TextButton introButton = new TextButton("Intro screen", skin);
        introButton.setColor(Color.BLACK);
         introButton.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 game.setScreen(new IntroScreen(game));
             }
         });

        TextButton playButton = new TextButton("Play", skin);
        playButton.setColor(Color.BLACK);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen((Screen) new GameScreen(game));
            }
        });


        TextButton leaderboardButton = new TextButton("Leaderboard", skin);
        leaderboardButton.setColor(Color.BLACK);
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.setColor(Color.BLACK);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.setColor(Color.BLACK);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        TextButton welcomePlayer = new TextButton("Welcome, " + GameManager.INSTANCE.getUsername(), skin);
        welcomePlayer.setColor(Color.BLACK);



        Table buttonTable = new Table();
        TextureRegion menuBackgroundRegion = gameplayAtlas2.findRegion(RegionNames.MENU_BACKGROUND);
        buttonTable.defaults().padLeft(40).padRight(40);
        buttonTable.setBackground(new TextureRegionDrawable(menuBackgroundRegion));
        buttonTable.add(welcomePlayer).padTop(60).row();
        buttonTable.add(playButton).padBottom(30).padTop(30).expandX().fillX().row();
        buttonTable.add(introButton).padBottom(30).expandX().fill().row();
        buttonTable.add(leaderboardButton).padBottom(30).fillX().row();
        buttonTable.add(settingsButton).padBottom(30).fillX().row();
        buttonTable.add(quitButton).padBottom(60).fillX().row();

        buttonTable.center();

        table.add(buttonTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
