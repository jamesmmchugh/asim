/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation.geneGroups.gene;

import java.util.ArrayList;

import com.tolk.asim.simulation.CACTION;
import com.tolk.asim.simulation.Creature;
import com.tolk.asim.simulation.Entity;
import com.tolk.asim.simulation.geneGroups.FeedingGene;

/**
 *
 * @author James
 */
public class MeatNearFirst implements FeedingGene {

    public MeatNearFirst() {
    }

    public int getDesire(Creature c) {
        int desire = 0;
        if(c.getHealth() == 100) {
            desire += 1;
        }
        if (c.getEnergy() < c.getDNA().getAttribute("targetMinEnergy")) {
            desire += 2;
        }
        if (c.getEnergy() < c.getDNA().getAttribute("targetStoreEnergy")) {
            desire += 2;
        }

        return desire;
    }

    public Entity nearestInSector(Creature c) {
        double distance = c.world.getSize();
        Entity e = null;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    ArrayList a = c.world.getCreatureCell((c.x / c.world.getCellSize()) + i, (c.y / c.world.getCellSize()) + j);
                    for (Object o : a) {
                        if (o instanceof Entity) {
                            if (c != o) {
                                Creature test = (Creature) o;
                                if (!test.getSpecies().equals(c.getSpecies()) && (test.alive || test.getKiller() == c)) {
                                    double temp = c.getDistance(test);
                                    if (temp < c.getDNA().getAttribute("siteRange") && temp < distance) {
                                        e = test;
                                        distance = c.getDistance(test);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }
        return e;
    }

    public CACTION chooseAction(Creature c) {
        Entity e = nearestInSector(c);
        if (e != null && e instanceof Creature) {
            Creature t = (Creature) e;
            c.setTarget(e);
            if (c.collidesWith(t)) {
                if (t.alive) {
                    return CACTION.FIGHTING;
                } else {
                    return CACTION.EATING;
                }
            } else {
                return CACTION.CHASING;
            }
        } else {
            c.setTarget(null);
            return CACTION.UNKOWN;
        }
    }
}
