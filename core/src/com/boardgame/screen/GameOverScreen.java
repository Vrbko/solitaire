package com.boardgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
import com.boardgame.screen.config.GameManager;
import java.util.Random;

public class GameOverScreen extends ScreenAdapter {

    private final BoardGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas2;
    private Texture background = new Texture((Gdx.files.internal("assets/MenuBackground.jpg")));

    public GameOverScreen(BoardGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        ScreenUtils.clear(0 / 255f, 128 / 255f, 0 / 255f, 0f);
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);

        gameplayAtlas2 = assetManager.get(AssetDescriptors.GAMEPLAY2);
        stage.addActor(createMenuButton());
        stage.addActor(createLeaderboardButton());
        stage.addActor(createPlayAgainButton());

        for (int i = 0; i < 5; i++)
            stage.addActor(createDeck(i));

        for (int i = 0; i < 10; i++) {
            for (int j = 6; j >= 0; j--) {
                if(i > 3 && j== 0)
                    continue;
                stage.addActor(createCard(i, j));
            }
        }

        for (int j = 0; j < 10; j++) {
            stage.addActor(createAnimation(j));
        }

        stage.addActor(gameOver());
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
    private Actor createPlayAgainButton() {
        final TextButton playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.setWidth(100);
        playAgainButton.setPosition(750, 500f);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RegionNames.resetCards();
                game.setScreen(new GameScreen(game));
            }
        });
        return playAgainButton;
    }
    private Actor createMenuButton() {
        final TextButton backButton = new TextButton("Main Menu", skin);
        backButton.setWidth(100);
        backButton.setPosition(750, 400f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RegionNames.resetCards();
                game.setScreen(new MenuScreen(game));
            }
        });

        return backButton;
    }
    private Actor createLeaderboardButton() {
        final TextButton leaderBoardButton = new TextButton("Leaderboard", skin);
        leaderBoardButton.setWidth(100);
        leaderBoardButton.setPosition(750, 450f);
        leaderBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RegionNames.resetCards();
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        return leaderBoardButton;
    }
    private Actor gameOver() {
        final Label gameText = new Label("WINNER", skin);

        gameText.setFontScale(10f);
        gameText.setColor(Color.RED);

        gameText.setPosition(450, 720f);


        return gameText;
    }

    private Actor createDeck(int offset) {
        Image deck = new Image(gameplayAtlas2.findRegion(RegionNames.CARD_BACKGROUND));
        deck.setScale(0.375f);
        deck.setPosition(GameConfig.HUD_WIDTH -250 - offset * 20, 70);
        final Random RANDOM = new Random();
        int randomAmount = RANDOM.nextInt(360);
        deck.addAction(

                Actions.sequence(
                        Actions.delay(2.5f),
                        Actions.delay(1.0f - offset*0.1f),
                        Actions.parallel(
                                Actions.rotateBy(randomAmount, 0.95f),
                                Actions.moveTo(80, 30, 0.9f)
                        )
                )
        );
        return deck;
    }

    private Actor createCard(int offsetX, int offsetY) {
        offsetX++;
        Image card;
        card = new Image(gameplayAtlas2.findRegion(RegionNames.CARD_BACKGROUND));
        card.setScale(0.375f);
        final Random RANDOM = new Random();
        int randomAmount = RANDOM.nextInt(360);
        card.setPosition(150*offsetX-100, 50*offsetY+400);
        card.addAction(

                Actions.sequence(
                        Actions.delay(2.0f),
                        Actions.delay(1.0f - offsetX * 0.1f - offsetY * 0.1f),
                        Actions.parallel(
                                 Actions.rotateBy(randomAmount, 0.95f),
                                Actions.moveTo(80, 30, 0.9f)
                        )
                )
        );
        return card;
    }

    private Actor createAnimation(int offset) {
        Image animatedCard = new Image(gameplayAtlas2.findRegion(RegionNames.returnRandomValue()));
        animatedCard.setScale(0.25f);
        float posX = 150*offset-100;
        float posY = 0;
        final Random RANDOM = new Random();
        int randomAmount = RANDOM.nextInt(360);
        if(offset > 3 )
            posY = 180;
        else
            posY = 135;
        animatedCard.setOrigin(Align.center);
        animatedCard.addAction(
                Actions.sequence(
                        Actions.moveTo(GameConfig.HUD_WIDTH -500, -152),
                        Actions.delay(1.0f * offset * 0.1f),
                        Actions.parallel(
                                Actions.rotateBy(720, 0.95f),
                                Actions.moveTo(posX, posY, 0.9f)
                        ),
                        Actions.delay(1.0f - offset * 0.1f),
                        Actions.parallel(
                                Actions.rotateBy(randomAmount, 0.95f),
                                Actions.moveTo(-120, -180, 0.9f)
                        )
                )
        );

        return animatedCard;
    }
}
