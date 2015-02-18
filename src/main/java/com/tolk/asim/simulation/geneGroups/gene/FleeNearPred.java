/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation.geneGroups.gene;

import java.util.ArrayList;

import com.tolk.asim.simulation.CACTION;
import com.tolk.asim.simulation.Creature;
import com.tolk.asim.simulation.Entity;
import com.tolk.asim.simulation.geneGroups.FleeingGene;

/**
 *
 * @author James
 */
public class FleeNearPred implements FleeingGene {

    public FleeNearPred() {
    }

    public int getDesire(Creature c) {
        int desire = 0;
        Entity nearestCreature = nearestInSector(c);
        if (nearestCreature != null) {
            double distance = c.getDistance(nearestCreature);
            if (distance <= c.getDNA().getAttribute("fleeRange")) {
                desire = (int)((double)distance / (double)c.getDNA().getAttribute("fleeRange") * 4);
            }
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
                                if (test.alive && !test.getSpecies().equals(c.getSpecies())) {
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
            c.setTarget(e);
            if (c.collidesWith(e)) {
                //return CACTION.FIGHTING;
                return CACTION.FLEEING;
                
            } else {
                return CACTION.FLEEING;
            }
        } else {
            c.setTarget(null);
            return CACTION.UNKOWN;
        }
    }
}
