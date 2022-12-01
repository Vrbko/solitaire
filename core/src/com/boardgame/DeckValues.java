package com.boardgame;

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
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
