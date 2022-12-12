package com.boardgame.screen.config;
//TODO

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.boardgame.BoardGame;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    private final Preferences PREFS;
    private boolean animation = false;
    private String username = "Unknown Soldier";

    private GameManager() {
        PREFS = Gdx.app.getPreferences(BoardGame.class.getSimpleName());
        String usernamePlayer = PREFS.getString("playerUsername", username);
        boolean animationsToggle = PREFS.getBoolean("animations", animation);

        this.username = usernamePlayer;
        this.animation = animationsToggle;

    }

    public void setName(String username) {
        this.username = username;
        PREFS.putString("playerUsername", username);
        PREFS.flush();
    }

    public void setAnimation(boolean toggle) {
        this.animation = toggle;
        PREFS.putBoolean("animations", animation);
        PREFS.flush();
    }

    public boolean isAnimation() {
        return animation;
    }

    public String getUsername() {
        return username;
    }

}
