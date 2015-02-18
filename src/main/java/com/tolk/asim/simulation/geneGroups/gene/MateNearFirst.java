/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation.geneGroups.gene;

import java.util.ArrayList;

import com.tolk.asim.simulation.CACTION;
import com.tolk.asim.simulation.CSTATE;
import com.tolk.asim.simulation.Creature;
import com.tolk.asim.simulation.Entity;
import com.tolk.asim.simulation.geneGroups.MatingGene;

/**
 *
 * @author James
 */
public class MateNearFirst implements MatingGene {

    public MateNearFirst() {
    }

    public int getDesire(Creature c) {
        int desire = 1;

        if (c.getEnergy() > c.getDNA().getAttribute("targetMinEnergy")) {
            desire += 1;
        }
        if (c.getFat() > c.getDNA().getAttribute("targetMinFat")) {
            desire += 1;
        }
        if (c.isFull()) {
            desire += 1;
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
                        if (o instanceof Creature) {
                            Creature t = (Creature) o;
                            if (t.alive && c != t) {
                                double temp = c.getDistance(t);
                                if (temp < c.getDNA().getAttribute("siteRange") && temp < distance) {
                                    if (t.getState() == CSTATE.HORNY) {
                                        e = t;
                                    }
                                    distance = c.getDistance(t);
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
            c.setTarget(e);
            if (c.collidesWith(e)) {
                return CACTION.MATING;
            } else {
                return CACTION.CHASING;
            }
        } else {
            c.setTarget(null);
            if (c.isFull()) {
                return CACTION.MATESEARCH;
            } else {
                return CACTION.PASSIVESEARCH;
            }
        }
    }
}
