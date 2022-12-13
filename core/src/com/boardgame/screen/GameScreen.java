package com.boardgame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
import com.boardgame.screen.config.GameConfig;
import com.boardgame.screen.config.GameManager;

public class GameScreen extends ScreenAdapter {

    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

    private final BoardGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Viewport hudViewport;
    private Stage gameplayStage;
    private Stage hudStage;
    private Skin skin;
    private Image infoImage;
    private TextureAtlas gameplayAtlas;

    private static final int cols = 10, rows = 20;
    private int  numberOfDecksCompleted = 0 ;
    private int destinationRow, destinationColumn;
    private int originRow, originColumn;
    private final int[][] Values = new int[25][10];

    private boolean click = true; // 1 - 0
    private final boolean[][] FacingValues = new boolean[20][10];

    final Table mainGrid = new Table();


    public GameScreen(BoardGame game) {
       // mainGrid.defaults().size()
        this.game = game;
        assetManager = game.getAssetManager();
        DeckValues.resetCards();
        for (int row = 0; row < rows; row++)
            for (int column = 0; column < cols; column++) {
                if(row == rows -1 )
                    FacingValues[rows-1][column] = true;
                if (row < 6) {
                    if (row == 5 && column > 3) {
                        Values[row][column] = 0;
                        FacingValues[row-1][column] = true;
                    }
                    else {
                        String x = DeckValues.returnRandomValue();
                        Values[row][column] = DeckValues.valueOf(x).ordinal();
                        // log.debug("Index: " + Values[row][column]);
                        FacingValues[row][column] = false;
                    }
                }
                else {
                    Values[row][column] = 0;
                    FacingValues[row-1][column] = true;
                }
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
        gameplayStage.setDebugAll(false);
        hudStage.addActor(createDecks());
        hudStage.addActor(createCompletedDecks());
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

        mainGrid.defaults().size(9f, 14f);   // all cells will be the same size
        mainGrid.setDebug(false);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++) {
                if(FacingValues[row][column])
                    cardValue[0] = gameplayAtlas.findRegion(DeckValues.values()[Values[row][column]].toString());
                else
                    cardValue[0] = gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND);

                final CardActor cell = new CardActor(cardValue[0], row, column);
                final TextureRegion finalCardValue = cardValue[0];
                final int[] finalRow = {row};
                final int finalColumn = column;

                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        final CardActor clickedCell = (CardActor) event.getTarget(); // it will be an image for sure :-)
                        if (Values[finalRow[0]][finalColumn] == 0 && finalRow[0] != 0){
                            /*int temp = getDestinationRow(finalColumn);
                            log.debug("TEMP " + temp + " row " + finalRow[0]);
                            if(finalRow[0] - temp <= 1) {
                                log.debug("YIPPIKAYYAY MOFO");
                                finalRow[0] = temp;
                            }
                            else*/
                         return;
                        }
                        if (clickedCell.isMovable()) {
                            if (click){
                                originRow = finalRow[0];
                                originColumn = finalColumn;
                                if (!doValuesDescend(originRow, originColumn)) { //improper
                                    clickedCell.improperSelection();
                                   // log.debug("improper");
                                }
                                else {
                                    click = !click;
                                    clickedCell.selectedAnimation();
                                  //  log.debug("proper");
                                }
                            }
                            else {
                                destinationColumn = finalColumn;
                                if(originColumn==destinationColumn)
                                    return;
                                updateValues(originRow, originColumn, destinationColumn);
                                SnapshotArray<Actor> children = mainGrid.getChildren();
                                int i = 0;
                                int j = 0;
                                for (Actor eex : children) {
                                    final CardActor tempValueForChangingTable = (CardActor) eex;
                                    if(FacingValues[j][i])
                                        cardValue[0] = gameplayAtlas.findRegion(DeckValues.values()[Values[j][i]].toString());
                                    else
                                        cardValue[0] = gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND);
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
                mainGrid.add(cell).padRight(2f).padLeft(2f); //2f
            }
            mainGrid.row().expand().padTop(-10f);//-10
        }
        mainGrid.pack();
        mainGrid.setPosition(13.5f, 0); // 13.0
        return mainGrid;
    }

    private Actor createBackButton() {
        final TextButton backButton = new TextButton("Back", skin);
        backButton.setWidth(100);
        backButton.setPosition(0, 20f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RegionNames.resetCards();


                GameManager.INSTANCE.updatePlayer(GameManager.INSTANCE.getUsername(),false);
                game.setScreen(new MenuScreen(game));
            }
        });

        return backButton;
    }

    private Actor createCompletedDecks() {
        final TextureRegion[] cardValue = {gameplayAtlas.findRegion(RegionNames.BLANC)};
        final Table table = new Table();
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)
        table.defaults().size(100f, 140f);
        final Table grid = new Table();
        grid.defaults().size(1f, 1f);   // all cells will be the same size
        grid.setDebug(false);
        int cols = 5;
        for (int column = 0; column < cols; column++) {
            final CardActor cell = new CardActor(cardValue[0], -1, column);
            final TextureRegion finalCardValue = cardValue[0];
            table.add(cell).padTop(-120f).row();
        }
        table.center();

        table.setFillParent(true);
        table.pack();
        table.setPosition(
                -740, -320
        );
        return table;
    }

    private Actor createDecks() {
        final TextureRegion[] cardValue = {gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND)};
        final Table table = new Table();
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)
        table.defaults().size(100f, 140f);
        final Table grid = new Table();
        grid.defaults().size(1f, 1f);   // all cells will be the same size
        grid.setDebug(false);
        int cols = 5;
        for (int column = 0; column < cols; column++) {
            final CardActor cell = new CardActor(cardValue[0], -1, column);
            final TextureRegion finalCardValue = cardValue[0];
            cell.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    final CardActor clickedCell = (CardActor) event.getTarget(); // it will be an image for sure :-)
                    if (clickedCell.isMovable()) {
                        clickedCell.removeAnimation();
                    }
                    // log.debug("clicked x: " + x + " y: " + x + " card: " + finalCardValue + " i: " + cell.returnIndexI() + " j: " + cell.returnIndexJ());
                    addOneCardToColumns();
                    SnapshotArray<Actor> children = mainGrid.getChildren();
                    int i = 0;
                    int j = 0;
                    for (Actor eex : children) {
                        final CardActor tempValueForChangingTable = (CardActor) eex;
                        if(FacingValues[j][i])
                            cardValue[0] = gameplayAtlas.findRegion(DeckValues.values()[Values[j][i]].toString());
                        else
                            cardValue[0] = gameplayAtlas.findRegion(RegionNames.CARD_BACKGROUND);
                        tempValueForChangingTable.setDrawable(cardValue[0]);

                        i++;
                        if (i % 10 == 0 && i != 0) {
                            i = 0;
                            j++;
                        }
                    }
                    isDeckCompleted(destinationColumn);
                }
            });
            table.add(cell).padTop(-90f).row();
        }
        table.center();

        table.setFillParent(true);
        table.pack();
        table.setPosition(
                740, -320
        );
        return table;
    }

    private boolean doValuesDescend(int originRowFunction, int originColumnFunction) {
        if (Values[originRowFunction + 1][originColumnFunction] == 0)
            return true;
        int temp = Values[originRowFunction][originColumnFunction];
        //  log.debug("\nHALLO: r: " + originRowFunction +" c: " + originColumnFunction +" t: "+ temp);
        for (int row = originRowFunction + 1; row < rows; row++) {
          //  log.debug("Temp:" + temp + "-1 == " + Values[row][originColumnFunction]);
            if (temp - 1 == Values[row][originColumnFunction]) {
                temp = Values[row][originColumnFunction];
                if (Values[row + 1][originColumnFunction] == 0)
                    return true;
            }
            else
                return false;
        }
        return false;
    }

    private void updateValues(int originRow, int originColumn, int destinationColumn) {
        if (originColumn == destinationColumn) {
          //  log.debug("RETURNED");
            return;
        }
        int destinationRow = getDestinationRow(destinationColumn);
        int offset = 0;
        if(destinationRow > 0 && Values[originRow][originColumn]+1  != Values[destinationRow-1][destinationColumn]){
        //    log.debug("org+1: " + Values[originRow][originColumn] + " !=  dest: " +  Values[destinationRow-1][destinationColumn]);
            return;
        }
        for (int row = destinationRow; row < rows; row++) {
            if (originRow + offset == 20)
                break;
            Values[row][destinationColumn] = Values[originRow + offset][originColumn];
          //  log.debug("ADDED: " + Values[row][destinationColumn] + " R:"  + row + " C: " + destinationColumn);
            offset++;
        }
        for (int row = originRow; row < rows; row++) {
            if(row == originRow && row != 0)
                FacingValues[row-1][originColumn] = true;
            //Values[destinationRow][destinationColumn] = Values[originRow][originColumn];
            Values[row][originColumn] = 0;
         //   log.debug("Removed: " + Values[row][originColumn] + " R:"  + row + " C: " + originColumn);
        }

        isDeckCompleted(destinationColumn);
        isGameEnd();

    }

    private void isGameEnd() {
        if(numberOfDecksCompleted == 8) {
            GameManager.INSTANCE.updatePlayer(GameManager.INSTANCE.getUsername(),true);
            game.setScreen((Screen) new GameOverScreen(game));
        }
        for(int i = 0 ;  i < cols ; i ++)
            if (Values[0][i] != 0)
               return;

        GameManager.INSTANCE.updatePlayer(GameManager.INSTANCE.getUsername(),true);
        game.setScreen((Screen) new GameOverScreen(game));
    }

    private void isDeckCompleted(int destinationColumn) {
        int kingValue = -1 ;
        for (int row = 0; row < rows; row++){
            if(Values[row][destinationColumn] == 13)
                kingValue = row;
        }
        int temp = 13;
        if(kingValue != -1) {
            for (int row = kingValue+1; row < rows; row++) {
                if(Values[row][destinationColumn] == temp -1 )
                    temp = Values[row][destinationColumn];
            }
            if( temp == 0) {
                for (int row = kingValue; row < rows; row++) {
                    Values[row][destinationColumn] = 0;
                   // log.debug("Removed: " + Values[row][destinationColumn]);
                }
                if(kingValue != 0) {
                    numberOfDecksCompleted++;
                    FacingValues[kingValue-1][destinationColumn] = true;
                   // FacingValues[kingValue-2][destinationColumn] = true;
                }
            }
            else
                log.debug("KOVK PA JE TEMP: " + temp);
        }
    }

    private int getDestinationRow(int destinationColumn) {
        for (int row = 0; row < rows; row++) {
            if (Values[row][destinationColumn] == 0)
                return row;
        }
        return 0;
    }

    private void addOneCardToColumns() {
        int lastIndex = 0;
        for (int col = 0; col < cols ; col++){
            lastIndex = getDestinationRow(col);
            String x = DeckValues.returnRandomValue();
            Values[lastIndex][col] =DeckValues.valueOf(x).ordinal();
        }

    }
}
