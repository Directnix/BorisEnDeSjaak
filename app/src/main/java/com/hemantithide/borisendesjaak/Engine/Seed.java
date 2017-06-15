package com.hemantithide.borisendesjaak.Engine;

import android.util.Log;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Daniel on 02/06/2017.
 */

public class Seed implements Serializable {

    LinkedList<LinkedList<Integer>> seedIntegers = new LinkedList<>();
    LinkedList<LinkedList<Double>> seedDoubles = new LinkedList<>();

    LinkedList<Integer> rockSeqA;

    public LinkedList<Integer> rockSeqB;
    LinkedList<Double> spawnChanceRockB;

    LinkedList<Integer> appleSeq;

    public LinkedList<Integer> kinkerSeq;
    LinkedList<Double> spawnChanceKinker;

    LinkedList<Integer> ducatSeq;

    public LinkedList<Integer> fireballSeq;


    public Seed() {
        make();
    }

    public Seed(SeedListener listener){
        make();
        listener.onSeedReady(this);
    }

    public Seed(String seedString, SeedListener listener) {

        Log.i("Received: ", seedString);

        seedIntegers.add(rockSeqA = new LinkedList<>());
        seedIntegers.add(rockSeqB = new LinkedList<>());
//        seedIntegers.add(appleSeq = new LinkedList<>());
//        seedIntegers.add(kinkerSeq = new LinkedList<>());
//        seedIntegers.add(ducatSeq = new LinkedList<>());
        seedIntegers.add(fireballSeq = new LinkedList<>());

        seedDoubles.add(spawnChanceRockB = new LinkedList<>());
        seedDoubles.add(spawnChanceKinker = new LinkedList<>());

        String[] in = seedString.split("<I>|\\<D>");

        int idx = 0;
        for (String s : in) {
            if(s.length() < 1)
                continue;

            s = s.substring(0, s.length()-1);
            String[] values = s.split(",");

            boolean doubleval = false;
            if (values[0].contains(".")) {
                if(idx == seedIntegers.size())
                    idx = 0;
                doubleval = true;
            }

            if(doubleval) {
                for (String value : values) {
                    if(value.equals(""))
                        continue;

                    value = value.replaceAll("[^\\d.]", "");
                    seedDoubles.get(idx).add(Double.valueOf(value));
                }
            } else {
                for (String value : values) {
                    if(value.equals(""))
                        continue;

                    value = value.replaceAll("[^\\d.]", "");
                    seedIntegers.get(idx).add(Integer.valueOf(value));
                }
            }

            idx++;
        }

        appleSeq = new LinkedList<>();
        kinkerSeq = new LinkedList<>();
        ducatSeq = new LinkedList<>();

        for (int i = 0; i < 1000; i++) {

            int randomNumberC;
            do randomNumberC = (int) Math.floor(Math.random() * 5);
            while (randomNumberC == rockSeqA.get(i));
            appleSeq.add(randomNumberC);

            int randomNumberD;
            do randomNumberD = (int) Math.floor(Math.random() * 5);
            while (randomNumberD == rockSeqB.get(i));
            ducatSeq.add(randomNumberD);

            int randomNumberE;
            do randomNumberE = (int) Math.floor(Math.random() * 5);
            while (randomNumberE == rockSeqA.get(i) || randomNumberE == randomNumberC);
            kinkerSeq.add(randomNumberE);
        }

        listener.onSeedReady(this);
    }

    public String getSeedString() {

        String res = "";

        for (LinkedList<Integer> list : seedIntegers) {
            res += "<I>";

            for (Number n : list) {
                res += n + ",";
            }
        }

        for (LinkedList<Double> list : seedDoubles) {
            res += "<D>";

            for (Number n : list) {
                res += n + ",";
            }
        }

        return res;
    }

    private void make(){
        seedIntegers.add(rockSeqA = new LinkedList<>());
        seedIntegers.add(rockSeqB = new LinkedList<>());

        appleSeq = new LinkedList<>();
        kinkerSeq = new LinkedList<>();
        ducatSeq = new LinkedList<>();
//        seedIntegers.add(appleSeq = new LinkedList<>());
//        seedIntegers.add(kinkerSeq = new LinkedList<>());
//        seedIntegers.add(ducatSeq = new LinkedList<>());

        seedIntegers.add(fireballSeq = new LinkedList<>());

        seedDoubles.add(spawnChanceRockB = new LinkedList<>());
        seedDoubles.add(spawnChanceKinker = new LinkedList<>());

        for (int i = 0; i < 1000; i++) {

            int randomNumberA = (int) Math.floor(Math.random() * 5);
            rockSeqA.add(randomNumberA);

            int randomNumberB;
            do randomNumberB = (int) Math.floor(Math.random() * 5);
            while (randomNumberA == randomNumberB);
            rockSeqB.add(randomNumberB);

            int randomNumberC;
            do randomNumberC = (int) Math.floor(Math.random() * 5);
            while (randomNumberC == randomNumberA);
            appleSeq.add(randomNumberC);

            int randomNumberD;
            do randomNumberD = (int) Math.floor(Math.random() * 5);
            while (randomNumberD == randomNumberB);
            ducatSeq.add(randomNumberD);

            int randomNumberE;
            do randomNumberE = (int) Math.floor(Math.random() * 5);
            while (randomNumberE == randomNumberC || randomNumberE == randomNumberA);
            kinkerSeq.add(randomNumberE);

            spawnChanceRockB.add(Math.random());
            spawnChanceKinker.add(Math.random());

            fireballSeq.add((int) Math.floor(Math.random() * 5));
        }
    }

    public interface SeedListener{
        void onSeedReady(Seed seed);
    }
}
