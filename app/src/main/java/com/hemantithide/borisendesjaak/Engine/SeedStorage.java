package com.hemantithide.borisendesjaak.Engine;

import java.util.LinkedList;

/**
 * Created by Daniel on 02/06/2017.
 */

public class SeedStorage {

    LinkedList<Integer> rockSeqA;

    LinkedList<Integer> rockSeqB;
    LinkedList<Double> spawnChanceRockB;

    LinkedList<Integer> appleSeq;

    LinkedList<Integer> kinkerSeq;
    LinkedList<Double> spawnChanceKinker;

    public LinkedList<Integer> fireballSeq;

    public SeedStorage() {

        rockSeqA = new LinkedList<>();
        rockSeqB = new LinkedList<>();
        spawnChanceRockB = new LinkedList<>();
        appleSeq = new LinkedList<>();
        kinkerSeq = new LinkedList<>();
        spawnChanceKinker = new LinkedList<>();
        fireballSeq = new LinkedList<>();

        for(int i = 0; i < 33000; i++) {

            int randomNumberA = (int)Math.floor(Math.random() * 5);
            rockSeqA.add(randomNumberA);

            int randomNumberB;
            do randomNumberB = (int)Math.floor(Math.random() * 5);
            while(randomNumberA == randomNumberB);
            rockSeqB.add(randomNumberB);

            int randomNumberC;
            do randomNumberC = (int)Math.floor(Math.random() * 5);
            while(randomNumberC == randomNumberA);
            appleSeq.add(randomNumberC);

            kinkerSeq.add((int)Math.floor(Math.random() * 5));

            spawnChanceRockB.add(Math.random());
            spawnChanceKinker.add(Math.random());

            fireballSeq.add((int)Math.floor(Math.random() * 5));
        }
    }
}
