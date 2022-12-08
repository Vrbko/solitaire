package com.boardgame;

import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum DeckValues {
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
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static DeckValues randomValue()  {
        DeckValues x = VALUES.get(RANDOM.nextInt(SIZE));
        usedValues.add(x);
        returnNumberOfOccurrences();
        return x;
    }
    private static final Logger log = new Logger(DeckValues.class.getSimpleName(), Logger.DEBUG);
    static final ArrayList<DeckValues> usedValues = new ArrayList<DeckValues>();

    public static void returnNumberOfOccurrences(){
        log.debug(String.valueOf(usedValues.size()));
    }

}
