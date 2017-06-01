package com.hemantithide.borisendesjaak;

import java.util.ArrayList;

/**
 * Created by thijs on 01-Jun-17.
 */

public class UsernameGenerator
{
    private ZNWoord randomZN;
    private BNWoord randomBN;
    private BNWoordBuiging randomBNBuiging;

    private String randomUsername;

    private ArrayList<ZNWoord> znWoordenArray;
    private ArrayList<BNWoord> bnWoordenArray;
    private ArrayList<BNWoordBuiging> bnWoordenBuigingArray;


    public String generateUsername()
    {
        randomZN = znWoordenArray.get((int)(Math.random()*znWoordenArray.size()));
        if(randomZN.heeftBuigingNodig == true)
        {
            randomBNBuiging = bnWoordenBuigingArray.get((int)(Math.random()*bnWoordenBuigingArray.size()));
            randomUsername = randomBNBuiging.getBNwoordMetBuiging() + " " + randomZN.getZNwoord();
        }
        else
        {
            randomBN = bnWoordenArray.get((int)(Math.random()*bnWoordenArray.size()));
            randomUsername = randomBN.getBNWoord() + " " + randomZN.getZNwoord();
        }


        return randomUsername;
    }

    public void addZNWoordentoArrayList()
    {
        znWoordenArray.add(new ZNWoord("Steen", true));
        znWoordenArray.add(new ZNWoord("Kikker", true));
        znWoordenArray.add(new ZNWoord("Prinses", true));
        znWoordenArray.add(new ZNWoord("Jong mals meisje", false));
        znWoordenArray.add(new ZNWoord("Koning", true));
        znWoordenArray.add(new ZNWoord("Koningin", true));
        znWoordenArray.add(new ZNWoord("Ridder", true));
        znWoordenArray.add(new ZNWoord("Postduif", true));
        znWoordenArray.add(new ZNWoord("Lans", true));
        znWoordenArray.add(new ZNWoord("Schaap", false));
        znWoordenArray.add(new ZNWoord("Lam", false));
        znWoordenArray.add(new ZNWoord("Vogel", true));
        znWoordenArray.add(new ZNWoord("Majesteit", true));
        znWoordenArray.add(new ZNWoord("Rakker", true));
        znWoordenArray.add(new ZNWoord("Schildknaap", true));
        znWoordenArray.add(new ZNWoord("Jager", true));
        znWoordenArray.add(new ZNWoord("DrakenJager", true));
        znWoordenArray.add(new ZNWoord("Tovernaar", true));
        znWoordenArray.add(new ZNWoord("Zwaard", false));
        znWoordenArray.add(new ZNWoord("Jedi", true));
        znWoordenArray.add(new ZNWoord("Draak", true));
        znWoordenArray.add(new ZNWoord("Pannenkoek", true));
        znWoordenArray.add(new ZNWoord("Python", true));
        znWoordenArray.add(new ZNWoord("Skelet", false));
        znWoordenArray.add(new ZNWoord("Spook", false));
        znWoordenArray.add(new ZNWoord("Schild", false));
        znWoordenArray.add(new ZNWoord("Hakbijl", true));
        znWoordenArray.add(new ZNWoord("Paard", false));
        znWoordenArray.add(new ZNWoord("Hengst", true));
        znWoordenArray.add(new ZNWoord("Merrie", true));
        znWoordenArray.add(new ZNWoord("Kraai", true));
        znWoordenArray.add(new ZNWoord("Jan", true));
        znWoordenArray.add(new ZNWoord("Gijs", true));
        znWoordenArray.add(new ZNWoord("Paul", true));
        znWoordenArray.add(new ZNWoord("Johan", true));
        znWoordenArray.add(new ZNWoord("Diederich", true));
        znWoordenArray.add(new ZNWoord("Nick", true));
        znWoordenArray.add(new ZNWoord("Mengtac", true));
        znWoordenArray.add(new ZNWoord("Thijs", true));
        znWoordenArray.add(new ZNWoord("Hein", true));
        znWoordenArray.add(new ZNWoord("Timo", true));
        znWoordenArray.add(new ZNWoord("Daniel", true));
        znWoordenArray.add(new ZNWoord("Smid", true));
        znWoordenArray.add(new ZNWoord("Schip", false));
        znWoordenArray.add(new ZNWoord("Hollander", true));
        znWoordenArray.add(new ZNWoord("VogelRok", true));
        znWoordenArray.add(new ZNWoord("Piranha", true));
        znWoordenArray.add(new ZNWoord("Boom", true));
        znWoordenArray.add(new ZNWoord("Paddenstoel", true));
    }

    public void addBNWoordenBuigingToArrayList()
    {
        bnWoordenBuigingArray.add(new BNWoordBuiging("Twijfelende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Vrolijke"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Boze"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Smerige"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Malse"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Slappe"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Sterke"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Grote"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Kleine"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Onverslagen"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Rode"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Groene"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Blause"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Gele"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Meester"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Vuur"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Water"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Holle Bolle"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Lange"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Angstaanjagende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Schubbige"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Opper"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Een"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Reizende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Slijmerige"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Papieren"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Houten"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Stalen"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Gouden"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Diamanten"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Lava"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("IJs"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Robot"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Gelaarsde"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Glazen"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Bliksem"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Draken"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Konings"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Kromme"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Vliegende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Schommelende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Draaiende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Sprookjes"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Onzichtbare"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Schreeuwende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Onsterfelijke"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Baby"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Geweldige"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Teleurstellende"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Thijs'"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Nick's"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Hein's"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Mengtac's"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Daniel's"));
        bnWoordenBuigingArray.add(new BNWoordBuiging("Timo's"));
    }

    public void addBNWoordenToArrayList()
    {
        bnWoordenArray.add(new BNWoord("Twijfelend"));
        bnWoordenArray.add(new BNWoord("Vrolijk"));
        bnWoordenArray.add(new BNWoord("Boos"));
        bnWoordenArray.add(new BNWoord("Smerig"));
        bnWoordenArray.add(new BNWoord("Mals"));
        bnWoordenArray.add(new BNWoord("Slap"));
        bnWoordenArray.add(new BNWoord("Sterk"));
        bnWoordenArray.add(new BNWoord("Groot"));
        bnWoordenArray.add(new BNWoord("Klein"));
        bnWoordenArray.add(new BNWoord("Onverslagen"));
        bnWoordenArray.add(new BNWoord("Rood"));
        bnWoordenArray.add(new BNWoord("Blauw"));
        bnWoordenArray.add(new BNWoord("Groen"));
        bnWoordenArray.add(new BNWoord("Geel"));
        bnWoordenArray.add(new BNWoord("Meester"));
        bnWoordenArray.add(new BNWoord("Vuur"));
        bnWoordenArray.add(new BNWoord("Water"));
        bnWoordenArray.add(new BNWoord("Holle Bolle"));
        bnWoordenArray.add(new BNWoord("Lang"));
        bnWoordenArray.add(new BNWoord("Angstaanjagend"));
        bnWoordenArray.add(new BNWoord("Schubbig"));
        bnWoordenArray.add(new BNWoord("Opper"));
        bnWoordenArray.add(new BNWoord("Reizend"));
        bnWoordenArray.add(new BNWoord("Slijmerig"));
        bnWoordenArray.add(new BNWoord("Papieren"));
        bnWoordenArray.add(new BNWoord("Houten"));
        bnWoordenArray.add(new BNWoord("Stalen"));
        bnWoordenArray.add(new BNWoord("Gouden"));
        bnWoordenArray.add(new BNWoord("Diamanten"));
        bnWoordenArray.add(new BNWoord("Lava"));
        bnWoordenArray.add(new BNWoord("IJs"));
        bnWoordenArray.add(new BNWoord("Robot"));
        bnWoordenArray.add(new BNWoord("Gelaarsd"));
        bnWoordenArray.add(new BNWoord("Glazen"));
        bnWoordenArray.add(new BNWoord("Bliksem"));
        bnWoordenArray.add(new BNWoord("Draken"));
        bnWoordenArray.add(new BNWoord("Konings"));
        bnWoordenArray.add(new BNWoord("Krom"));
        bnWoordenArray.add(new BNWoord("Vliegend"));
        bnWoordenArray.add(new BNWoord("Schommelend"));
        bnWoordenArray.add(new BNWoord("Draaiend"));
        bnWoordenArray.add(new BNWoord("Sprookjes"));
        bnWoordenArray.add(new BNWoord("Onzichtbaar"));
        bnWoordenArray.add(new BNWoord("Schreeuwend"));
        bnWoordenArray.add(new BNWoord("Onsterfelijk"));
        bnWoordenArray.add(new BNWoord("Baby"));
        bnWoordenArray.add(new BNWoord("Geweldig"));
        bnWoordenArray.add(new BNWoord("Teleurstellend"));
        bnWoordenArray.add(new BNWoord("Thijs'"));
        bnWoordenArray.add(new BNWoord("Nick's"));
        bnWoordenArray.add(new BNWoord("Mengtac's"));
        bnWoordenArray.add(new BNWoord("Daniel's"));
        bnWoordenArray.add(new BNWoord("Hein's"));
        bnWoordenArray.add(new BNWoord("Timo's"));
    }



    //inner class voor zelfstandige naamwoorden
    public class ZNWoord
    {
        private String ZNwoord;
        private boolean heeftBuigingNodig;


        public ZNWoord(String ZN, boolean needsBuiging)
        {
            this.ZNwoord = ZN;
            heeftBuigingNodig = needsBuiging;
        }

        public boolean getHeeftBuigingNodig()
        {
            return heeftBuigingNodig;
        }

        public String getZNwoord()
        {
            return ZNwoord;
        }
    }


    //inner class voor bijvoegelijke naamwoorden met buiging
    public class BNWoordBuiging
    {
        private String BNwoordMetBuiging;

        public BNWoordBuiging(String bnWoordmetBuiging)
        {
            this.BNwoordMetBuiging = bnWoordmetBuiging;
        }

        public String getBNwoordMetBuiging()
        {
            return BNwoordMetBuiging;
        }
    }



    //inner class voor bijvoegelijke naamwoorden
    public class BNWoord
    {
        private String BNWoord;

        public BNWoord(String bnWoord)
        {
            this.BNWoord = bnWoord;
        }

        public String getBNWoord()
        {
            return BNWoord;
        }
    }
}
