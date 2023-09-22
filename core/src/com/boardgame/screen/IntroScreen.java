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

        gameplayAtlas2 = assetManager.get(AssetDescriptors.GAMEPLAY2);

        for (int i = 0; i < 5; i++)
            stage.addActor(createDeck(i));

        for (int i = 9; i >= 0; i--) {
            for (int j = 6; j >= 0; j--) {
                if(i > 3 && j== 0)
                    continue;
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
        deck.setScale(0.375f);
        deck.setPosition(GameConfig.HUD_WIDTH -250 - offset * 20, 70);

        return deck;
    }

    private Actor createCard(int offsetX, int offsetY) {
        offsetX++;
        Image card;
        card = new Image(gameplayAtlas2.findRegion(RegionNames.CARD_BACKGROUND));
        card.setScale(0.375f);

        card.setPosition(150*offsetX-100, 50*offsetY+400);
        return card;
    }

    private Actor createAnimation(int offset) {
        Image animatedCard = new Image(gameplayAtlas2.findRegion(RegionNames.returnRandomValue()));
        animatedCard.setScale(0.25f);
        float posX = 150*offset-100;
        float posY = 0;
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
                        )
                )
        );

        return animatedCard;
    }
}

