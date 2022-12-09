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
    public static final String CARD_BACKGROUND= "card_background"; //preimenuj lepo
    public static final String MENU_BACKGROUND = "menu_background";
    public static final String ACE = "A";
    public static final String CARD_2= "2";
    public static final String CARD_3= "3";
    public static final String CARD_4= "4";
    public static final String CARD_5= "5";
    public static final String CARD_6= "6";
    public static final String CARD_7= "7";
    public static final String CARD_8= "8";
    public static final String CARD_9= "9";
    public static final String CARD_10= "10";
    public static final String JACK= "J";
    public static final String QUEEN= "Q";
    public static final String KING = "K";

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
        }while(occurrences  > 4);

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
