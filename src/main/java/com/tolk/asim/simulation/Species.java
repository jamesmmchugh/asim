/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author James
 */
public class Species {

    public String name;
    public Color colour;
    public ArrayList<Creature> creatures;
    public DNA startingDNA;
    public DNA averageDNA;

    public Species(World w, String name) {
        this.name = name;
        Random r = new Random();
        this.colour = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
        this.creatures = new ArrayList();
        w.addSpecies(this);
        averageDNA = new DNA();
    }

    public Species(World w, String name, DNA dna) {
        this.name = name;
        Random r = new Random();
        this.colour = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
        this.creatures = new ArrayList();
        this.startingDNA = dna;
        w.addSpecies(this);
        averageDNA = new DNA();
        updateAverage();
    }

    public void updateAverage() {
        int[] ndna = averageDNA.getAttributes();
        for (int i = 0; i < ndna.length; i++) {
            ndna[i] = 0;
        }
        for (Creature c : creatures) {
            int[] cdna = c.getDNA().getAttributes();
            for (int i = 0; i < ndna.length; i++) {
                ndna[i] += cdna[i];
            }
        }
        for (int i = 0; i < ndna.length; i++) {
            if (creatures.size() > 0) {
                ndna[i] /= creatures.size();
            }
        }
        averageDNA.setAttributes(ndna);
    }

    public void addCreature(Creature c) {
        if (!creatures.contains(c)) {
            creatures.add(c);
        }
        updateAverage();
    }

    public void removeCreature(Creature c) {
        if (creatures.contains(c)) {
            creatures.remove(c);
        }
        updateAverage();
    }

    public int population() {
        int p = 0;
        for (Creature c : creatures) {
            if (c.alive) {
                p++;
            }
        }
        return p;
    }
}
