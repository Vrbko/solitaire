package com.boardgame.screen;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.DARK_GRAY;
import static com.badlogic.gdx.graphics.Color.GREEN;
import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.VIOLET;
import static com.badlogic.gdx.graphics.Color.YELLOW;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.CardActor;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.config.GameConfig;
import com.boardgame.screen.config.GameManager;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

public class SettingsScreen extends ScreenAdapter {

    private final BoardGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private TextButton toggleButton;
    private TextButton toggleAudioButton;
    private TextField username;


    public SettingsScreen(BoardGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());
        stage.setDebugAll(false);
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

        Skin uiSkin = assetManager.get(AssetDescriptors.UI_SKIN);
        TextureAtlas gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY2);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        String tempUsername = GameManager.INSTANCE.getUsername();
        username = new TextField(tempUsername, uiSkin);
        username.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                if(focused)
                  username.setText("");
            }
        });

        toggleButton = new TextButton("", uiSkin, "toggle");
        if(!GameManager.INSTANCE.isAnimation()){
            toggleButton.toggle();
            toggleButton.setText("Animacije: OFF");
        }
        else
            toggleButton.setText("Animacije: ON");

        toggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setName(username.getText());
                if(toggleButton.isChecked()) {
                    GameManager.INSTANCE.setAnimation(false);
                    toggleButton.setText("Animacije: OFF");
                }
                else {
                    GameManager.INSTANCE.setAnimation(true);
                    toggleButton.setText("Animacije: ON");
                }


            }
        });


        toggleAudioButton = new TextButton("", uiSkin, "toggle");
        if(!GameManager.INSTANCE.isAudio()){
            toggleAudioButton.toggle();
            toggleAudioButton.setText("Musi");
        }
        else
            toggleAudioButton.setText("Music");

        toggleAudioButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setName(username.getText());
                if(toggleAudioButton.isChecked()) {
                    GameManager.INSTANCE.setAudio(false);
                    GameManager.INSTANCE.stopMusic();
                    toggleAudioButton.setText("Music");
                }
                else {
                    GameManager.INSTANCE.setAudio(true);
                    GameManager.INSTANCE.startMusic();
                    toggleAudioButton.setText("Music");
                }


            }
        });

        TextButton backButton = new TextButton("Back", uiSkin);
        backButton.setColor(BLACK);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(username.getText().equals("") )
                    username.setText("Unknown Soldier");
                GameManager.INSTANCE.setName(username.getText());

                GameManager.INSTANCE.addPlayer(username.getText());

                game.setScreen(new MenuScreen(game));

            }
        });

        TextButton nextButton = new TextButton("->", uiSkin);
        TextButton prevButton = new TextButton("<-", uiSkin);






        nextButton.setColor(BLACK);
        prevButton.setColor(BLACK);

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.nextSong();

            }
        });
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.prevSong();

            }
        });


        //TextureRegion menuBackground = gameplayAtlas.findRegion(RegionNames.MENU_BACKGROUND);

        Table contentTable = new Table(uiSkin);
        contentTable.setDebug(true);
        //contentTable.setBackground(new TextureRegionDrawable(menuBackground));
        contentTable.add(toggleButton).colspan(3).row();



        contentTable.add(prevButton);
        contentTable.add(toggleAudioButton);
        contentTable.add(nextButton);
        contentTable.row().expand();

       // for(Music track: GameManager.INSTANCE.returnMusic()){
         //   contentTable.add(new TextButton(track.toString(),uiSkin)).colspan(3).row();
        //}
        //toDO i guess v nov objekt k deduje iz music pa ime pesmi dodat


        contentTable.add(username).padLeft(30).padRight(30).padTop(30).colspan(3).row();
        contentTable.add(backButton).width(100).padTop(30).padBottom(20).colspan(3);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
