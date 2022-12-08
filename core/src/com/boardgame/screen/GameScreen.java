package com.boardgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.CardActor;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.config.GameConfig;


public class GameScreen extends ScreenAdapter {

    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

    private final BoardGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Viewport hudViewport;

    private Stage gameplayStage;
    private Stage hudStage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private Image infoImage;

    public GameScreen(BoardGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        gameplayStage.addActor(initialSpawn());
        //hudStage.addActor(createInfo());
        hudStage.addActor(createBackButton());

        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0 / 255f, 128 / 255f, 0 / 255f, 0f);

        // update
        gameplayStage.act(delta);
        hudStage.act(delta);

        // draw
        gameplayStage.draw();
        hudStage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        gameplayStage.dispose();
        hudStage.dispose();
    }



    private Actor initialSpawn() {
        Stack stack = new Stack();
        TextureRegion cardValue = gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND);
         TextureRegion xRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
         Table table = new Table();
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)
        table.setTouchable(Touchable.enabled);
        final Table grid = new Table();
        grid.defaults().size(4,8);   // all cells will be the same size
        grid.setDebug(true);
        int rows= 6, cols = 10 ;


        for (int row = 0; row < rows ; row++) {
            for (int column = 0; column < cols; column++) {

                if(row > 3 && column > 3 || row > 4)
                    cardValue  = gameplayAtlas.findRegion(RegionNames.returnRandomValue());
                if(row == 5 && column == 4 )
                    break;
                final CardActor cell = new CardActor(cardValue,row,column);
                final TextureRegion finalCardValue = cardValue;
                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        log.debug("clicked x: " + x +" y: " + x + " card: " + finalCardValue + " i: " + cell.returnIndexI() + " j: " + cell.returnIndexJ());
                    }
                });

                grid.add(cell).padRight(1.7f).padLeft(1.7f);


            }
            grid.row().expand().padTop(-6);
            stack.add(grid);
        }

        table.add(grid).row();
        table.center();

        table.setFillParent(true);
        table.pack();
        table.setPosition(
                0,20
        );

        return table;
    }

    private Actor createBackButton() {
        final TextButton backButton = new TextButton("Back", skin);
        backButton.setWidth(100);
        backButton.setPosition(GameConfig.HUD_WIDTH / 2f - backButton.getWidth() / 2f, 20f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        return backButton;
    }

    private Actor createInfo() {
        final Table table = new Table();
      //  table.add(new Label("Turn: ", skin));
        //table.add(infoImage).size(30).row();
        table.center();
        table.pack();
        table.setPosition(
                GameConfig.HUD_WIDTH / 2f - table.getWidth() / 2f,
                GameConfig.HUD_HEIGHT - table.getHeight() - 208f
        );
        return table;
    }
}
