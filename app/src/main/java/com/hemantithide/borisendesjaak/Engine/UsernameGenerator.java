package com.hemantithide.borisendesjaak.Engine;

import java.util.ArrayList;

import static com.hemantithide.borisendesjaak.Engine.UsernameGenerator.Gender.*;

/**
 * Created by thijs on 01-Jun-17.
 */

public class UsernameGenerator
{
    private int age;

    public enum Gender { MALE, FEMALE }
    private Gender gender;

    private Noun randomNoun;
    private Adjective randomAdjectiveA;
    private Adjective randomAdjectiveB;

    private ArrayList<Noun> nounArray = new ArrayList<>();
    private ArrayList<Adjective> firstAdjectiveArray = new ArrayList<>();
    private ArrayList<Adjective> secondAdjectiveArray = new ArrayList<>();


    public String generateUsername(int age, Gender gender)
    {
        this.age = age;
        this.gender = gender;

        initNounArray();
        initAdjectiveArrays();

        randomNoun = nounArray.get((int)(Math.random()* nounArray.size()));

        randomAdjectiveA = firstAdjectiveArray.get(((int) (Math.random() * nounArray.size())));

        do      randomAdjectiveB = secondAdjectiveArray.get(((int)(Math.random()* nounArray.size())));
        while   (randomAdjectiveA.equals(randomAdjectiveB));

        String firstAdjective = randomAdjectiveA.adj;
        String secondAdjective = randomAdjectiveB.adj;
        String noun = randomNoun.noun;

        if(randomNoun.needsBending) {
            firstAdjective = randomAdjectiveA.bentAdj;
            secondAdjective = randomAdjectiveB.bentAdj;
        }

        String adjectives = firstAdjective + " " + secondAdjective;
        if(firstAdjective.equals(secondAdjective)) {
            adjectives = firstAdjective + ", " + secondAdjective;
        }

        if(randomAdjectiveB.needsSpace) {
            return adjectives + " " + noun;
        } else {
            return adjectives + noun.toLowerCase();
        }

    }

    public void initNounArray()
    {
        nounArray.add(new Noun("Kikker", true));
        nounArray.add(new Noun("Kinker", true));
        nounArray.add(new Noun("Steen", true));
        nounArray.add(new Noun("Postduif", true));
        nounArray.add(new Noun("Schaap", false));
        nounArray.add(new Noun("Lammetje", false));
        nounArray.add(new Noun("Vogel", true));;
        nounArray.add(new Noun("Majesteit", true));
        nounArray.add(new Noun("Rakker", true));
        nounArray.add(new Noun("Schildknaap", true));
        nounArray.add(new Noun("Jager", true));
        nounArray.add(new Noun("Tovenaar", true));
        nounArray.add(new Noun("Zwaard", false));
        nounArray.add(new Noun("Jedi", true));
        nounArray.add(new Noun("Draak", true));
        nounArray.add(new Noun("Pannenkoek", true));
        nounArray.add(new Noun("Python", true));
        nounArray.add(new Noun("Hakbijl", true));
        nounArray.add(new Noun("Paard", false));
        nounArray.add(new Noun("Kraai", true));
        nounArray.add(new Noun("Smid", true));
        nounArray.add(new Noun("Vogelrok", true));
        nounArray.add(new Noun("Piranha", true));
        nounArray.add(new Noun("Boom", true));
        nounArray.add(new Noun("Paddenstoel", true));
        nounArray.add(new Noun("Lans", true));
        nounArray.add(new Noun("Reus", true));
        nounArray.add(new Noun("Piraat", true));
        nounArray.add(new Noun("Wafel", true));

        if(gender == MALE) {
            nounArray.add(new Noun("Koning", true));
            nounArray.add(new Noun("Ridder", true));
            nounArray.add(new Noun("Hengst", true));
            nounArray.add(new Noun("Jan", true));

        } else if(gender == FEMALE) {
            nounArray.add(new Noun("Prinses", true));
            nounArray.add(new Noun("Meisje", false));
            nounArray.add(new Noun("Koningin", true));
            nounArray.add(new Noun("Merrie", true));
        }
    }

    public void initAdjectiveArrays()
    {

        firstAdjectiveArray.add(new Adjective("Thijs'", true));
        firstAdjectiveArray.add(new Adjective("Nick's", true));
        firstAdjectiveArray.add(new Adjective("Mengtac's", true));
        firstAdjectiveArray.add(new Adjective("Daniel's", true));
        firstAdjectiveArray.add(new Adjective("Hein's", true));
        firstAdjectiveArray.add(new Adjective("Timo's", true));
        firstAdjectiveArray.add(new Adjective("Johan's", true));
        firstAdjectiveArray.add(new Adjective("Paul's", true));
        firstAdjectiveArray.add(new Adjective("Diederich's", true));

        secondAdjectiveArray.add(new Adjective("Vuur", false));
        secondAdjectiveArray.add(new Adjective("Water", false));
        secondAdjectiveArray.add(new Adjective("Lava", false));
        secondAdjectiveArray.add(new Adjective("IJs", false));
        secondAdjectiveArray.add(new Adjective("Bliksem", false));
        secondAdjectiveArray.add(new Adjective("Opper", "Opper", false));
        secondAdjectiveArray.add(new Adjective("Konings", "Konings", false));
        secondAdjectiveArray.add(new Adjective("Meester", false));

        secondAdjectiveArray.add(new Adjective("Papieren", true));
        secondAdjectiveArray.add(new Adjective("Houten", true));
        secondAdjectiveArray.add(new Adjective("Stalen", true));
        secondAdjectiveArray.add(new Adjective("Gouden", true));
        secondAdjectiveArray.add(new Adjective("Diamanten", true));
        secondAdjectiveArray.add(new Adjective("Robot", true));
        secondAdjectiveArray.add(new Adjective("Glazen", true));

        firstAdjectiveArray.add(new Adjective("Twijfelend", "Twijfelende", true));
        firstAdjectiveArray.add(new Adjective("Vrolijk", "Vrolijke", true));
        firstAdjectiveArray.add(new Adjective("Mals", "Malse", true));
        firstAdjectiveArray.add(new Adjective("Boos", "Boze", true));
        firstAdjectiveArray.add(new Adjective("Sterk", "Sterke", true));
        firstAdjectiveArray.add(new Adjective("Groot", "Grote", true));
        firstAdjectiveArray.add(new Adjective("Klein", "Kleine", true));
        firstAdjectiveArray.add(new Adjective("Onverslagen", true));
        firstAdjectiveArray.add(new Adjective("Rood", "Rode", true));
        firstAdjectiveArray.add(new Adjective("Blauw", "Blauwe", true));
        firstAdjectiveArray.add(new Adjective("Groen", "Groene", true));
        firstAdjectiveArray.add(new Adjective("Geel", "Gele", true));
        firstAdjectiveArray.add(new Adjective("Lang", "Lange", true));
        firstAdjectiveArray.add(new Adjective("Angstaanjagend", "Angstaanjagende", true));
        firstAdjectiveArray.add(new Adjective("Schubbig", "Schubbige", true));
        firstAdjectiveArray.add(new Adjective("Reizend", "Reizende", true));
        firstAdjectiveArray.add(new Adjective("Slijmerig", "Slijmerige", true));
        firstAdjectiveArray.add(new Adjective("Gelaarsd", "Gelaarsde", true));
        firstAdjectiveArray.add(new Adjective("Krom", "Kromme", true));
        firstAdjectiveArray.add(new Adjective("Vliegend", "Vliegende", true));
        firstAdjectiveArray.add(new Adjective("Schommelend", "Schommelende", true));
        firstAdjectiveArray.add(new Adjective("Draaiend", "Draaiende", true));
        firstAdjectiveArray.add(new Adjective("Onzichtbaar", "Onzichtbaar", true));
        firstAdjectiveArray.add(new Adjective("Schreeuwend", "Schreeuwend", true));
        firstAdjectiveArray.add(new Adjective("Onsterfelijk", "Onsterfelijke", true));
        firstAdjectiveArray.add(new Adjective("Geweldig", "Geweldige", true));
        firstAdjectiveArray.add(new Adjective("Gespierd", "Gespierde", true));
        firstAdjectiveArray.add(new Adjective("Mooi", "Mooie", true));
        firstAdjectiveArray.add(new Adjective("Lief", "Lieve", true));
        ///
        secondAdjectiveArray.add(new Adjective("Twijfelend", "Twijfelende", true));
        secondAdjectiveArray.add(new Adjective("Vrolijk", "Vrolijke", true));
        secondAdjectiveArray.add(new Adjective("Mals", "Malse", true));
        secondAdjectiveArray.add(new Adjective("Boos", "Boze", true));
        secondAdjectiveArray.add(new Adjective("Sterk", "Sterke", true));
        secondAdjectiveArray.add(new Adjective("Groot", "Grote", true));
        secondAdjectiveArray.add(new Adjective("Klein", "Kleine", true));
        secondAdjectiveArray.add(new Adjective("Onverslagen", true));
        secondAdjectiveArray.add(new Adjective("Rood", "Rode", true));
        secondAdjectiveArray.add(new Adjective("Blauw", "Blauwe", true));
        secondAdjectiveArray.add(new Adjective("Groen", "Groene", true));
        secondAdjectiveArray.add(new Adjective("Geel", "Gele", true));
        secondAdjectiveArray.add(new Adjective("Lang", "Lange", true));
        secondAdjectiveArray.add(new Adjective("Angstaanjagend", "Angstaanjagende", true));
        secondAdjectiveArray.add(new Adjective("Schubbig", "Schubbige", true));
        secondAdjectiveArray.add(new Adjective("Reizend", "Reizende", true));
        secondAdjectiveArray.add(new Adjective("Slijmerig", "Slijmerige", true));
        secondAdjectiveArray.add(new Adjective("Gelaarsd", "Gelaarsde", true));
        secondAdjectiveArray.add(new Adjective("Krom", "Kromme", true));
        secondAdjectiveArray.add(new Adjective("Vliegend", "Vliegende", true));
        secondAdjectiveArray.add(new Adjective("Schommelend", "Schommelende", true));
        secondAdjectiveArray.add(new Adjective("Draaiend", "Draaiende", true));
        secondAdjectiveArray.add(new Adjective("Onzichtbaar", "Onzichtbaar", true));
        secondAdjectiveArray.add(new Adjective("Schreeuwend", "Schreeuwend", true));
        secondAdjectiveArray.add(new Adjective("Onsterfelijk", "Onsterfelijke", true));
        secondAdjectiveArray.add(new Adjective("Baby", true));
        secondAdjectiveArray.add(new Adjective("Geweldig", "Geweldige", true));
        secondAdjectiveArray.add(new Adjective("Gespierd", "Gespierde", true));
        secondAdjectiveArray.add(new Adjective("Mooi", "Mooie", true));
        secondAdjectiveArray.add(new Adjective("Lief", "Lieve", true));

        if(age >= 16) {
            firstAdjectiveArray.add(new Adjective("Smerig", "Smerige", true));
            secondAdjectiveArray.add(new Adjective("Smerig", "Smerige", true));

            firstAdjectiveArray.add(new Adjective("Slap", "Slappe", true));
            secondAdjectiveArray.add(new Adjective("Slap", "Slappe", true));

            firstAdjectiveArray.add(new Adjective("Teleurstellend", "Teleurstellende", true));
            secondAdjectiveArray.add(new Adjective("Teleurstellend", "Teleurstellende", true));

            firstAdjectiveArray.add(new Adjective("Middelmatig", "Middelmatige", true));
            secondAdjectiveArray.add(new Adjective("Middelmatig", "Middelmatige", true));
        }



        // voeg hier nieuwe bijv. naamwoorden toe

        firstAdjectiveArray.add(new Adjective("Vriendelijk", "Vriendelijke", true));
        secondAdjectiveArray.add(new Adjective("Vriendelijk", "Vriendelijke", true));

        firstAdjectiveArray.add(new Adjective("Verlegen", true));
        secondAdjectiveArray.add(new Adjective("Verlegen", true));

        secondAdjectiveArray.add(new Adjective("Super", false));

        secondAdjectiveArray.add(new Adjective("Mega", false));

        firstAdjectiveArray.add(new Adjective("Enorm", false));
        firstAdjectiveArray.add(new Adjective("Oneindig", false));
    }



    //inner class voor zelfstandige naamwoorden
    private class Noun
    {
        String noun;
        boolean needsBending;


        Noun(String noun, boolean needsBending)
        {
            this.noun = noun;
            this.needsBending = needsBending;
        }
    }


    //inner class voor bijvoegelijke naamwoorden met buiging
    public class BNWoordBuiging
    {
        private String BNwoordMetBuiging;
        private boolean canOnlyBeFirst;

        public BNWoordBuiging(String bnWoordmetBuiging, boolean canOnlyBeFirst)
        {
            this.BNwoordMetBuiging = bnWoordmetBuiging;
            this.canOnlyBeFirst = canOnlyBeFirst;
        }

        public String getBNwoordMetBuiging()
        {
            return BNwoordMetBuiging;
        }
    }



    //inner class voor bijvoegelijke naamwoorden
    private class Adjective {
        String adj;
        String bentAdj;
        boolean needsSpace;

        private Adjective(String adj, boolean needsSpace) {
            this.adj = adj;
            this.bentAdj = adj;
            this.needsSpace = needsSpace;
        }

        private Adjective(String adj, String bentAdj, boolean needsSpace) {
            this.adj = adj;
            this.bentAdj = bentAdj;
            this.needsSpace = needsSpace;
        }

        @Override
        public String toString() {
            return adj;
        }
    }
}
