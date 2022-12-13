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
import com.boardgame.Player;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.config.GameConfig;
import com.boardgame.screen.config.GameManager;


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
        table.defaults();

        TextureRegion backgroundRegion = gameplayAtlas2.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        backButton.setColor(Color.BLACK);



        final Label gameUserText = new Label("Username", skin);
        final Label gameTriesText = new Label("Tries", skin);
        final Label gameWinsText = new Label("Wins", skin);
        gameUserText.setColor(Color.BLACK);
        gameTriesText.setColor(Color.BLACK);
        gameWinsText.setColor(Color.BLACK);

        gameUserText.setFontScale(1.5f);
        gameTriesText.setFontScale(1.5f);
        gameWinsText.setFontScale(1.5f);

        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);
        TextureRegion menuBackgroundRegion = gameplayAtlas2.findRegion(RegionNames.MENU_BACKGROUND);
        buttonTable.setBackground(new TextureRegionDrawable(menuBackgroundRegion));
       // List list = new List(skin);

        buttonTable.add(gameUserText).padTop(40);
        buttonTable.add(gameWinsText).padTop(40);
        buttonTable.add(gameTriesText).padTop(40);
        buttonTable.row().expand();

        for (Player temp: GameManager.INSTANCE.getPlayerList()){
            final Label gameUser = new Label(temp.getUsername(), skin);
            final Label gameTries = new Label(GameManager.INSTANCE.getTries(temp.getUsername()), skin);
            final Label gameWins = new Label(GameManager.INSTANCE.getScore(temp.getUsername()), skin);
            gameUser.setColor(Color.RED);
            gameTries.setColor(Color.RED);
            gameWins.setColor(Color.RED);
            gameUser.setFontScale(1.5f);
            gameTries.setFontScale(1.5f);
            gameWins.setFontScale(1.5f);

            buttonTable.add(gameUser);
            buttonTable.add(gameWins);
            buttonTable.add(gameTries);
            buttonTable.row().padTop(-460f).expand();

        }





       // list.getSelection().setMultiple(true);
        //list.getSelection().setRequired(false);

      //  buttonTable.add(list).padTop(30).padBottom(30).padTop(30).expandX().fillX().row();
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
