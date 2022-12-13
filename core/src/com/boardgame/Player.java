package com.boardgame;

import java.util.Objects;

public class Player {
    private String username = "";
    private int wins;
    private int nr_tries;


    public Player(String username){
        this.username =username;
    }
    public Player(String username, int wins, int nr_tries){
        this.username =username;
        this.wins = wins;
        this.nr_tries = nr_tries;
    }


    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getNr_tries() {
        return nr_tries;
    }

    public void setNr_tries(int nr_tries) {
        this.nr_tries = nr_tries;
    }

    public float getPercentage() {
        return (float)wins/nr_tries;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Player)) {
            return false;
        }
        Player player = (Player)obj;
        return Objects.equals(this.username, player.getUsername());
    }

}
