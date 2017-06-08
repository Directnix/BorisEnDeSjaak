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

        seedIntegers.add(rockSeqA = new LinkedList<>());
        seedIntegers.add(rockSeqB = new LinkedList<>());
        seedIntegers.add(appleSeq = new LinkedList<>());
        seedIntegers.add(kinkerSeq = new LinkedList<>());
        seedIntegers.add(ducatSeq = new LinkedList<>());
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

            kinkerSeq.add((int) Math.floor(Math.random() * 5));

            spawnChanceRockB.add(Math.random());
            spawnChanceKinker.add(Math.random());

            fireballSeq.add((int) Math.floor(Math.random() * 5));
        }
    }

    public Seed(String seedString) {

        seedIntegers.add(rockSeqA = new LinkedList<>());
        seedIntegers.add(rockSeqB = new LinkedList<>());
        seedIntegers.add(appleSeq = new LinkedList<>());
        seedIntegers.add(kinkerSeq = new LinkedList<>());
        seedIntegers.add(ducatSeq = new LinkedList<>());
        seedIntegers.add(fireballSeq = new LinkedList<>());

        seedDoubles.add(spawnChanceRockB = new LinkedList<>());
        seedDoubles.add(spawnChanceKinker = new LinkedList<>());

        String[] in = seedString.split("<I>|\\<D>");

        int idx = 0;
        for (String s : in) {
            String[] values = s.split(",");

            boolean doubleval = false;
            if (values[0].contains("."))
                doubleval = true;

            if(doubleval) {
                for (String value : values)
                    seedDoubles.get(idx).add(Double.valueOf(value));
            } else {
                for (String value : values)
                    seedIntegers.get(idx).add(Integer.valueOf(value));
            }

            idx++;
        }


        // TODO: 08/06/2017 Overloaded constructor when seedString is given
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

//        Log.e("Sent: ", res + "");
    }
}
