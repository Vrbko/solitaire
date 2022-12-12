package com.boardgame.assets;
import com.badlogic.gdx.utils.Logger;
import com.boardgame.DeckValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class RegionNames {

    public static final String BACKGROUND = "menu";
    public static final String CARD_BACKGROUND= "test2";
    public static final String CARD_BACKGROUND_INTRO= "card_background";
    public static final String MENU_BACKGROUND = "car_background-test";
    public static final String BLANC = "blank";

    public static final String ACE = "ACE";
    public static final String CARD_2= "TWO";
    public static final String CARD_3= "THREE";
    public static final String CARD_4= "FOUR";
    public static final String CARD_5= "FIVE";
    public static final String CARD_6= "SIX";
    public static final String CARD_7= "SEVEN";
    public static final String CARD_8= "EIGHT";
    public static final String CARD_9= "NINE";
    public static final String CARD_10= "TEN";
    public static final String JACK= "JACK";
    public static final String QUEEN= "QUEEN";
    public static final String KING = "KING";


    static List<String> list = Arrays.asList(ACE, JACK, QUEEN,KING,CARD_2,CARD_3,CARD_4,CARD_5,CARD_6,CARD_7,CARD_8,CARD_9,CARD_10);
    private static final int SIZE = list.size();
    private static final Random RANDOM = new Random();
    private RegionNames() {
    }

    public static String returnRandomValue()  {
        String x="";
        int occurrences = 0 ;
        do{
             x = list.get(RANDOM.nextInt(SIZE));
             occurrences = Collections.frequency(usedValues, x);
        }while(occurrences  > 8);
        if(occurrences == 8)
            log.debug("Occurrences: " + String.valueOf(occurrences) + "of " + x);
        usedValues.add(x);
        //returnNumberOfOccurrences();
        return x;
    }
    private static final Logger log = new Logger(DeckValues.class.getSimpleName(), Logger.DEBUG);
    static final ArrayList<String> usedValues = new ArrayList<String>();

    public static void returnNumberOfOccurrences(){
        log.debug(String.valueOf(usedValues.size()));
    }
    public static void resetCards() {
        usedValues.clear();
    }

}
