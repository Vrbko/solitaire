package com.boardgame.screen.config;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Logger;
import com.boardgame.BoardGame;
import com.boardgame.Player;
import com.boardgame.screen.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);
    private final Preferences PREFS;
    private boolean animation = false;

    private boolean audio = false;
    private String username = "Unknown Soldier";
    private ArrayList<Player> listPlayers ;
    private Json json ;
    private FileHandle file ;

    private Music menuMusic;


    private GameManager() {
        file = Gdx.files.local("scores.json");
        json = new Json();
        listPlayers = new ArrayList<Player>();

        JsonReader jsonReader = new JsonReader();
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("teufel.ogg"));

        if(file.exists()){
        JsonValue values = jsonReader.parse(Gdx.files.internal(file.path()));
        for (JsonValue value : values) {
            Player temp = new Player(value.get("username").asString(), value.get("wins").asInt(), value.get("nr_tries").asInt());
            listPlayers.add(temp);
        }
        Collections.sort(listPlayers, new Comparator<Player>(){
            public int compare(Player s1, Player s2) {
                return s2.getWins() - s1.getWins();
            }
        });
        }
        PREFS = Gdx.app.getPreferences(BoardGame.class.getSimpleName());
        String usernamePlayer = PREFS.getString("playerUsername", username);
        boolean animationsToggle = PREFS.getBoolean("animations", animation);
        boolean audioToggle = PREFS.getBoolean("audio", audio);
        this.username = usernamePlayer;
        this.animation = animationsToggle;

        this.audio = audioToggle;

        if(this.audio)
            this.startMusic();
    }
    public void stopMusic() {
        if(menuMusic.isPlaying())
            menuMusic.stop();
    }

    public void startMusic() {
        menuMusic.setLooping(true);
        menuMusic.play();

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
    public void setAudio(boolean toggle) {
        this.audio = toggle;
        if(toggle){

        }
        PREFS.putBoolean("audio", audio);
        PREFS.flush();
    }

    public boolean isAnimation() {
        return animation;
    }

    public boolean isAudio() {
        return audio;
    }

    public String getUsername() {
        return username;
    }

    public void addPlayer(String name) {
        Player temp = new Player(name);
        if(!listPlayers.contains(temp))
            listPlayers.add(temp);
        log.debug("SIZE" + listPlayers.size());

        file.writeString(json.toJson(listPlayers), false);

    }

    public void updatePlayer(String username, boolean b) {
        int index = listPlayers.indexOf(new Player(username));
        Player temp = listPlayers.get(index);
        temp.setNr_tries(temp.getNr_tries()+1);
        if(b)
            temp.setWins(temp.getWins()+1);
        listPlayers.set(index,temp);

        file.writeString(json.toJson(listPlayers), false);
    }


    public String getScore(String username) {
        int index = listPlayers.indexOf(new Player(username));
        Player temp = listPlayers.get(index);
        return ""+temp.getWins();

    }

    public String getTries(String username) {
        int index = listPlayers.indexOf(new Player(username));
        Player temp = listPlayers.get(index);
        return ""+temp.getNr_tries();
    }

    public ArrayList<Player> getPlayerList() {
        return listPlayers;
    }

    public CharSequence getPrecentage(String username) {
        int index = listPlayers.indexOf(new Player(username));
        Player temp = listPlayers.get(index);
        return ""+temp.getPercentage();
    }
}
