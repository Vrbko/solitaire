package com.boardgame.screen.config;


import static java.lang.Math.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
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
import java.util.Random;

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

    private final Music menuMusic,menuMusic2,menuMusic3,menuMusic4,menuMusic5;
    private final Array<Music> musicLibrary ;



    private GameManager() {
        file = Gdx.files.local("scores.json");
        json = new Json();
        listPlayers = new ArrayList<Player>();
        musicLibrary = new Array<Music>();
        JsonReader jsonReader = new JsonReader();
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/teufel.ogg"));
        menuMusic2 = Gdx.audio.newMusic(Gdx.files.internal("music/wirSind.ogg"));
        menuMusic3 = Gdx.audio.newMusic(Gdx.files.internal("music/teufelPiano.ogg"));
        menuMusic4 = Gdx.audio.newMusic(Gdx.files.internal("music/trinken.ogg"));
        menuMusic5 = Gdx.audio.newMusic(Gdx.files.internal("music/apocalypse.ogg"));

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
        musicLibrary.add(menuMusic);
        musicLibrary.add(menuMusic2);
        musicLibrary.add(menuMusic3);
        musicLibrary.add(menuMusic4);
        musicLibrary.add(menuMusic5);

        if(this.audio)
            this.startMusic();



        for (int i= 0; i < this.musicLibrary.size ; i ++) {

                musicLibrary.get(i).setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        nextSong();

                    }
                });

        }

    }
    public void stopMusic() {
        for(Music track: this.musicLibrary){
            if(track.isPlaying())
                track.stop();
        }
    }
    public Array<Music> returnMusic(){
        return musicLibrary;
    }

    public void nextSong() {
        boolean play = false;
        short iterator = 0;
        for(Music track: this.musicLibrary){
            iterator++;
            if(play) {
                track.play();

                break;
            }
            if(track.isPlaying()) {
                track.stop();
                play = true;
                if(iterator == this.musicLibrary.size){
                    this.musicLibrary.get(0).play();
                }
            }
        }
    }
    public void prevSong() {
        for(short i = 0 ; i < this.musicLibrary.size ; i++){
            try {

                if (this.musicLibrary.get(i).isPlaying()) {

                    musicLibrary.get(i).stop();
                    if (i == 0) {
                        this.musicLibrary.get(this.musicLibrary.size-1).play();

                        break;


                    } else{
                        this.musicLibrary.get(i - 1).play();
                        break;
                    }


                }
            }
            catch (Exception e){
                log.debug(e.toString());
            }
        }
    }

    public void startMusic() {
        Random rand= new Random();
        musicLibrary.get(rand.nextInt(musicLibrary.size)).play();
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
