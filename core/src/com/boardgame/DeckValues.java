package com.boardgame;

import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum DeckValues {
    blank,
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;
    private static final List<DeckValues> VALUES =
            Arrays.asList(ACE, JACK, QUEEN,KING,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN);
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static String returnRandomValue()  {
        DeckValues x;
        int occurrences = 0 ;
        do{
            x = VALUES.get(RANDOM.nextInt(SIZE));
            occurrences = Collections.frequency(usedValues, x);
        }while(occurrences  > 8);

        //log.debug("Occurrences: " + String.valueOf(occurrences) + "of " + x);
        usedValues.add(x);
        //returnNumberOfOccurrences();
        return x.toString();
    }
    private static final Logger log = new Logger(DeckValues.class.getSimpleName(), Logger.DEBUG);
    static  ArrayList<DeckValues> usedValues = new ArrayList<DeckValues>();

    public static void returnNumberOfOccurrences(){
        log.debug(String.valueOf(usedValues.size()));
    }

    public static void resetCards() {
        usedValues.clear();
    }
}
