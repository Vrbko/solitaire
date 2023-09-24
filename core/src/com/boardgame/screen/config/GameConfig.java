package com.boardgame.screen.config;

import com.badlogic.gdx.Gdx;

/**
 * Be careful which 'GameConfig' class you use in a specific examples. This class is 'public' because
 * of demonstration purposes. In this way, you can use it in all packages inside gameplay and also in
 * Desktop Launcher for this example. But because it is set to 'public' you can also access it from
 * other examples in different packages. Be careful which class/package you include inside an example.
 */
public class GameConfig {



    public static final float HUD_WIDTH = Gdx.graphics.getWidth(); // pixels
    public static final float HUD_HEIGHT = Gdx.graphics.getHeight();    // pixels

    public static final float WORLD_WIDTH = Gdx.graphics.getWidth()/10f;    // world units
    public static final float WORLD_HEIGHT = Gdx.graphics.getHeight()/10f;   // world units

    private GameConfig() {
    }
}

