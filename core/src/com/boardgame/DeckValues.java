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
        }while(occurrences  > 7);



        usedValues.add(x);

        log.debug("ace " +  Collections.frequency(usedValues, DeckValues.ACE));
        log.debug("2 " +  Collections.frequency(usedValues, DeckValues.TWO));
        log.debug("3 " +  Collections.frequency(usedValues, DeckValues.THREE));
        log.debug("4 " +  Collections.frequency(usedValues, DeckValues.FOUR));
        log.debug("5 " +  Collections.frequency(usedValues, DeckValues.FIVE));
        log.debug("6 " +  Collections.frequency(usedValues, DeckValues.SIX));
        log.debug("7 " +  Collections.frequency(usedValues, DeckValues.SEVEN));
        log.debug("8 " +  Collections.frequency(usedValues, DeckValues.EIGHT));
        log.debug("9 " +  Collections.frequency(usedValues, DeckValues.NINE));
        log.debug("10 " +  Collections.frequency(usedValues, DeckValues.TEN));
        log.debug("j " +  Collections.frequency(usedValues, DeckValues.JACK));
        log.debug("q " +  Collections.frequency(usedValues, DeckValues.QUEEN));
        log.debug("k " +  Collections.frequency(usedValues, DeckValues.KING));
        log.debug("Size " + usedValues.size());
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