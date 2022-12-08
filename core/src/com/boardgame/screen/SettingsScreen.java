package com.boardgame.screen;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.DARK_GRAY;
import static com.badlogic.gdx.graphics.Color.GREEN;
import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.VIOLET;
import static com.badlogic.gdx.graphics.Color.YELLOW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.config.GameConfig;
import com.boardgame.screen.config.GameManager;

public class SettingsScreen extends ScreenAdapter {

    private final BoardGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private TextButton toggleButton;
    private TextField username;

    public SettingsScreen(BoardGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());
        stage.setDebugAll(true);
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

        Skin uiSkin = assetManager.get(AssetDescriptors.UI_SKIN);
        TextureAtlas gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));


        username = new TextField("Username", uiSkin);
        username.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                if(focused)
                  username.setText("");
            }
        });
        toggleButton = new TextButton("Animacije", uiSkin, "toggle");
        //toggleButton.setColor(BLACK);

        TextButton backButton = new TextButton("Back", uiSkin);
        backButton.setColor(BLACK);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setName(username.getText());
                if(toggleButton.isChecked())
                    GameManager.INSTANCE.setAnimation(true);
                else
                    GameManager.INSTANCE.setAnimation(false);
                game.setScreen(new MenuScreen(game));

            }
        });

        TextureRegion menuBackground = gameplayAtlas.findRegion(RegionNames.MENU_BACKGROUND);

        Table contentTable = new Table(uiSkin);
        contentTable.setBackground(new TextureRegionDrawable(menuBackground));
        contentTable.add(toggleButton).padTop(20).row();
        contentTable.add(username).padLeft(30).padRight(30).padTop(30).row();
        contentTable.add(backButton).width(100).padTop(30).padBottom(20).colspan(3);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
