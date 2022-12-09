package com.boardgame.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boardgame.BoardGame;
import com.boardgame.DeckValues;
import com.boardgame.assets.AssetDescriptors;
import com.boardgame.assets.RegionNames;
import com.boardgame.screen.config.GameConfig;


public class IntroScreen extends ScreenAdapter {
    public static final float INTRO_DURATION_IN_SEC = 2.5f;   // duration of the (intro) animation

    private final BoardGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas2;

    private float duration = 0f;

    private Stage stage;

    public IntroScreen(BoardGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // load assets



        gameplayAtlas2 = assetManager.get(AssetDescriptors.GAMEPLAY);

        for (int i = 0; i < 5; i++)
            stage.addActor(createDeck(i));


        for (int i = 0; i < 10; i++) {
            for (int j = 1; j < 4; j++) {
                stage.addActor(createCard(i, j));
            }
        }

        for (int j = 0; j < 10; j++) {
            stage.addActor(createAnimation(j));
        }



    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0 / 255f, 128 / 255f, 0 / 255f, 0f);

        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC) {
            RegionNames.resetCards();
            game.setScreen(new MenuScreen(game));
        }

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

    private Actor createDeck(int offset) {
        Image deck = new Image(gameplayAtlas2.findRegion(RegionNames.CARD_BACKGROUND));
        deck.setPosition(viewport.getWorldWidth() - deck.getWidth() * 2 - deck.getWidth() * offset * .5f,
                viewport.getWorldHeight() / 4f / 2f - deck.getHeight() / 2f);

        return deck;
    }

    private Actor createCard(int offsetX, int offsetY) {
        Image card;
        card = new Image(gameplayAtlas2.findRegion(RegionNames.CARD_BACKGROUND));


        card.setPosition(viewport.getWorldWidth() - 40 - card.getWidth() - (offsetX * 2f * card.getWidth()),
                viewport.getWorldHeight() - card.getHeight() - card.getHeight() / 4 * offsetY * 0.75f);
        return card;
    }

    private Actor createAnimation(int offset) {
        Image animatedCard = new Image(gameplayAtlas2.findRegion(RegionNames.returnRandomValue()));
        float posX = viewport.getWorldWidth() - 40 - animatedCard.getWidth() - (offset * 2f * animatedCard.getWidth());
        float posY = (viewport.getWorldHeight()) - animatedCard.getHeight() * 1.75f;
        animatedCard.setOrigin(Align.center);
        animatedCard.addAction(
                Actions.sequence(
                        Actions.moveTo((viewport.getWorldWidth()) - animatedCard.getWidth() * 4.5f, animatedCard.getHeight() / 2f - 10),
                        Actions.delay(1.0f - 0.1f * offset),
                        Actions.parallel(
                                Actions.rotateBy(720, 0.95f),   // rotate the image three times
                                Actions.moveTo(posX, posY, 0.9f)   // // move image to the center of the window
                        )
                        // Actions.removeActor()   // // remove image
                )
        );

        return animatedCard;
    }
}

