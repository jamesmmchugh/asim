/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation.geneGroups.gene;

import java.util.ArrayList;

import com.tolk.asim.simulation.CACTION;
import com.tolk.asim.simulation.Creature;
import com.tolk.asim.simulation.Entity;
import com.tolk.asim.simulation.Plant;
import com.tolk.asim.simulation.geneGroups.FeedingGene;

/**
 *
 * @author James
 */
public class VegNearFirst implements FeedingGene {

    public VegNearFirst() {
    }

    public int getDesire(Creature c) {
        int desire = 1;
        if(!c.isFull()) {
            desire += 1;
            if (c.getEnergy() < c.getDNA().getAttribute("targetMinEnergy")) {
                desire += 1;
            }
            if (c.getEnergy() < c.getDNA().getAttribute("targetStoreEnergy")) {
                desire += 1;
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
                    ArrayList a = c.world.getPlantCell((c.x / c.world.getCellSize()) + i, (c.y / c.world.getCellSize()) + j);
                    for (Object o : a) {
                        if (o instanceof Entity) {
                            Entity t = (Entity) o;
                            if (t.alive && c != t) {
                                double temp = c.getDistance(t);
                                if (temp < c.getDNA().getAttribute("siteRange") && temp < distance) {
                                    e = t;
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
        if (e != null && e instanceof Plant) {
            Plant p = (Plant) e;
            if (p.isInUse() == null || p.isInUse() == c) {
                p.use(c);
                c.setTarget(e);
                if (c.collidesWith(e)) {
                    return CACTION.EATING;
                } else {
                    return CACTION.CHASING;
                }
            } else {
                c.setTarget(null);
                return CACTION.UNKOWN;
            }
        } else {
            c.setTarget(null);
            return CACTION.UNKOWN;
        }
    }
}
