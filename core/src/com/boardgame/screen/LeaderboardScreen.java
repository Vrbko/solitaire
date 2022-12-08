package com.boardgame.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.config.GameConfig;


public class LeaderboardScreen extends ScreenAdapter {

    private final BoardGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas2;

    public LeaderboardScreen(BoardGame game) {
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

        TextButton backButton = new TextButton("Nazaj", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        backButton.setColor(Color.BLACK);
        Object[] listEntries = {"Vrbko: 192 - 1m55s", "Joza: 202 - 2m15s", "Franci: 303 - 4m22s","Brozo: 665 - 11m05s","Adam: 1024 - 22m44s"};


        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);
        TextureRegion menuBackgroundRegion = gameplayAtlas2.findRegion(RegionNames.MENU_BACKGROUND);
        buttonTable.setBackground(new TextureRegionDrawable(menuBackgroundRegion));
        List list = new List(skin);
        list.setItems(listEntries);
        list.setColor(Color.BLACK);


        list.getSelection().setMultiple(true);
        list.getSelection().setRequired(false);

        buttonTable.add(list).padTop(30).padBottom(30).padTop(30).expandX().fillX().row();
        buttonTable.add(backButton).padBottom(30).padLeft(15).padRight(15).expandX().row();
        //buttonTable.add(playButton).padBottom(15).expandX().fill().row();


        buttonTable.center();


        table.add(buttonTable).padBottom(100).padTop(100).fillX().row();

        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
