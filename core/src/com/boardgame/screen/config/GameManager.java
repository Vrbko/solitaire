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
    }

    public void setName(String username) {
        this.username = username;
        PREFS.putString("playerUsername", username);
        PREFS.flush();
    }

    public void setAnimation(boolean toggle) {
        this.animation = toggle;
        PREFS.putString("animations", toString(animation));
        PREFS.flush();
    }

    private String toString(boolean animation) {
        if(animation)
            return "true";
        else
            return "false";
    }

    public String getUsername() {
        return username;
    }
}
