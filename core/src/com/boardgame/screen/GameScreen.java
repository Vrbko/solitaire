package com.boardgame.screen;

import static com.boardgame.assets.RegionNames.returnRandomValue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.CardActor;

import com.boardgame.DeckValues;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.MenuScreen;
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

    private boolean click = false; // 1 - 0
    private int destinationRow,destinationColumn;
    private int originRow, originColumn;

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

        gameplayStage.addActor(createGrid());
        hudStage.addActor(createDecks());
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

    private Actor createGrid() {

        TextureRegion cardValue = gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND);
        final TextureRegion xRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        final Table table = new Table();
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)
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
                final int finalRow = row;
                final int finalColumn = column;



                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        final CardActor clickedCell = (CardActor) event.getTarget(); // it will be an image for sure :-)
                        
                        //log.debug(String.valueOf(table.swapActor(1,3)));
                        if(click){
                            originRow = finalRow;
                            originColumn = finalColumn;
                        }
                        if(!click){
                            destinationRow = finalRow;
                            destinationColumn = finalColumn;
                        }
                            click = !click;
                        if (clickedCell.isMovable()) {

                           clickedCell.setDrawable(xRegion);
                        }


                        log.debug("clicked x: " + x +" y: " + x + " card: " + finalCardValue + " i: " + cell.returnIndexI() + " j: " + cell.returnIndexJ());
                    }
                });

                grid.add(cell).padRight(1.7f).padLeft(1.7f);


            }
            grid.row().expand().padTop(-6);

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
                RegionNames.resetCards();
                game.setScreen(new MenuScreen(game));
            }
        });

        return backButton;
    }

    private Actor createDecks() {
        TextureRegion cardValue = gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND);
        final TextureRegion xRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        final Table table = new Table();
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)
        final Table grid = new Table();
        grid.defaults().size(40,80);   // all cells will be the same size
        grid.setDebug(false);
        int cols = 5 ;


            for (int column = 0; column < cols; column++) {
                final CardActor cell = new CardActor(cardValue,0,column);
                final TextureRegion finalCardValue = cardValue;
                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        final CardActor clickedCell = (CardActor) event.getTarget(); // it will be an image for sure :-)
                        if (clickedCell.isMovable()) {
                            clickedCell.setDrawable(xRegion);
                        }
                        log.debug("clicked x: " + x +" y: " + x + " card: " + finalCardValue + " i: " + cell.returnIndexI() + " j: " + cell.returnIndexJ());
                        table.clear();


                    }
                });

                grid.add(cell).padLeft(-10.7f);

            }



        table.add(grid).row();
        table.center();

        table.setFillParent(true);
        table.pack();
        table.setPosition(
               250,-200
        );
        return table;
    }
}
