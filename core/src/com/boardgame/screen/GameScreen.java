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
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.CardActor;

import com.boardgame.DeckValues;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.MenuScreen;
import com.boardgame.screen.config.GameConfig;
import com.sun.tools.javac.util.Log;

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
    private static final int cols = 10, rows = 20;
    private Image infoImage;

    private boolean click = true; // 1 - 0
    private int destinationRow, destinationColumn;
    private int originRow, originColumn;

    private int[][] Values = new int[20][10];

    public GameScreen(BoardGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        DeckValues.resetCards();
        for (int row = 0; row < rows; row++)
            for (int column = 0; column < cols; column++) {

                if (row < 2) {
                    String x = DeckValues.returnRandomValue();
                    Values[row][column] = DeckValues.valueOf(x).ordinal();
                    log.debug("Index: " + Values[row][column]);
                    if (row == 5 && column > 3)
                        Values[row][column] = 0;
                } else
                    Values[row][column] = 0;
            }

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
       // gameplayStage.setDebugAll(false);
        hudStage.addActor(createDecks());
        hudStage.addActor(createBackButton());


        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        log.debug("resize" + height + " ," + width);
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

        final TextureRegion[] cardValue = {gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND)};
        final TextureRegion xRegion = gameplayAtlas.findRegion(RegionNames.BLANC);

        final Table grid = new Table();
        grid.defaults().size(7f, 14f);   // all cells will be the same size
        grid.setDebug(false);


        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++) {
                // if(row > 3 && column > 3 || row > 4)
                cardValue[0] = gameplayAtlas.findRegion(DeckValues.values()[Values[row][column]].toString());

                final CardActor cell = new CardActor(cardValue[0], row, column);
                final TextureRegion finalCardValue = cardValue[0];
                final int finalRow = row;
                final int finalColumn = column;


                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        final CardActor clickedCell = (CardActor) event.getTarget(); // it will be an image for sure :-)
                        if (Values[finalRow][finalColumn] == 0 && finalRow != 0)
                            return;
                        if (clickedCell.isMovable()) {
                            if (click) {
                                originRow = finalRow;
                                originColumn = finalColumn;
                                if (!doValuesDescend(originRow, originColumn)) { //improper
                                    clickedCell.improperSelection();
                                    log.debug("improper");
                                } else {
                                    click = !click;
                                    clickedCell.selectedAnimation();
                                    log.debug("proper");
                                }
                            } else {
                                destinationColumn = finalColumn;
                                log.debug("tu? - orgrow: " + originRow + " orgcol: " + originColumn + "destcol: " + destinationColumn);
                                if(originColumn==destinationColumn)
                                    return;
                                log.debug("tu?1");
                                updateValues(originRow, originColumn, destinationColumn);
                                log.debug("tu?2 ");
                                SnapshotArray<Actor> children = grid.getChildren();
                                int i = 0;
                                int j = 0;
                                for (Actor eex : children) {
                                    final CardActor tempValueForChangingTable = (CardActor) eex;
                                    cardValue[0] = gameplayAtlas.findRegion(DeckValues.values()[Values[j][i]].toString());
                                    tempValueForChangingTable.setDrawable(cardValue[0]);
                                    i++;
                                    if (i % 10 == 0 && i != 0) {
                                        i = 0;
                                        j++;
                                    }
                                }
                                click = !click; // IF PROPER CLICK, ELSE
                            }
                        }


                        //  log.debug("click:"  + click + "clicked x: " + x +" y: " + x + " card: " + finalCardValue + " i: " + cell.returnIndexI() + " j: " + cell.returnIndexJ()+ " final " + finalColumn);
                    }
                });

                grid.add(cell).padRight(2f).padLeft(2f);


            }
            grid.row().expand().padTop(-10);

        }


        // grid.center();

        grid.pack();
        grid.setPosition(30, 0);

        return grid;
    }

    private boolean doValuesDescend(int originRowFunction, int originColumnFunction) {
        if (Values[originRowFunction + 1][originColumnFunction] == 0)
            return true;
        int temp = Values[originRowFunction][originColumnFunction];
        int here = getDestinationRow(originColumnFunction);
        //  log.debug("\nHALLO: r: " + originRowFunction +" c: " + originColumnFunction +" t: "+ temp);
        for (int row = originRowFunction + 1; row < rows; row++) {
            log.debug("Temp:" + temp + "-1 == " + Values[row][originColumnFunction]);
            if (temp - 1 == Values[row][originColumnFunction]) {
                temp = Values[row][originColumnFunction];
                if (Values[row + 1][originColumnFunction] == 0)
                    return true;
            } else
                return false;


        }

        return false;
    }


    private void updateValues(int originRow, int originColumn, int destinationColumn) {
        if (originColumn == destinationColumn) {
            log.debug("RETURNED");
            return;
        }
        int destinationRow = getDestinationRow(destinationColumn);
        int offset = 0;
        log.debug("vrjetno tu");
        if(destinationRow > 0 && Values[originRow][originColumn]+1  != Values[destinationRow-1][destinationColumn]){
            log.debug("org+1: " + Values[originRow][originColumn] + " !=  dest: " +  Values[destinationRow-1][destinationColumn]);
            return;
        }



        for (int row = destinationRow; row < rows; row++) {
            if (originRow + offset == 20)
                break;
            Values[row][destinationColumn] = Values[originRow + offset][originColumn];
            log.debug("ADDED: " + Values[row][destinationColumn] + " R:"  + row + " C: " + destinationColumn);
            offset++;
        }

        for (int row = originRow; row < rows; row++) {
            //Values[destinationRow][destinationColumn] = Values[originRow][originColumn];
            Values[row][originColumn] = 0;
            log.debug("Removed: " + Values[row][originColumn] + " R:"  + row + " C: " + originColumn);
        }

    }

    private int getDestinationRow(int destinationColumn) {
        for (int row = 0; row < rows; row++) {
            if (Values[row][destinationColumn] == 0)
                return row;
        }
        return 0;
    }


    private Actor createBackButton() {
        final TextButton backButton = new TextButton("Back", skin);
        backButton.setWidth(100);
        backButton.setPosition(0, 20f);
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
        grid.defaults().size(40, 80);   // all cells will be the same size
        grid.setDebug(false);
        int cols = 5;
        for (int column = 0; column < cols; column++) {
            final CardActor cell = new CardActor(cardValue, 0, column);
            final TextureRegion finalCardValue = cardValue;
            cell.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    final CardActor clickedCell = (CardActor) event.getTarget(); // it will be an image for sure :-)
                    if (clickedCell.isMovable()) {
                        clickedCell.setDrawable(xRegion);
                    }
                    log.debug("clicked x: " + x + " y: " + x + " card: " + finalCardValue + " i: " + cell.returnIndexI() + " j: " + cell.returnIndexJ());
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
                750, -300
        );
        return table;
    }
}
