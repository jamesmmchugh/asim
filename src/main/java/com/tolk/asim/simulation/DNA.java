/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

import java.util.ArrayList;

import com.tolk.asim.simulation.geneGroups.FeedingGene;
import com.tolk.asim.simulation.geneGroups.FleeingGene;
import com.tolk.asim.simulation.geneGroups.MatingGene;
import com.tolk.asim.simulation.geneGroups.gene.FleeNearPred;
import com.tolk.asim.simulation.geneGroups.gene.MateNearFirst;
import com.tolk.asim.simulation.geneGroups.gene.VegNearFirst;


/**
 *
 * @author James
 */
public class DNA {

    private ArrayList<Attribute> attributes;
    public FeedingGene hungerGene = new VegNearFirst();
    public FleeingGene scaredGene = new FleeNearPred();
    public MatingGene hornyGene = new MateNearFirst();

    public DNA() {
        attributes = new ArrayList();
        attributes.add(new Attribute("fleeRange",0,1000,600));
        attributes.add(new Attribute("targetMinEnergy",0,Creature.MAX_ENERGY,100));

        attributes.add(new Attribute("targetMinFat", 0, Creature.MAX_FAT, 100));

        attributes.add(new Attribute("targetStoreEnergy",0,Creature.MAX_FAT,300));
        attributes.add(new Attribute("strength",0,100,40));
        attributes.add(new Attribute("metabolismRate",0,100,50));
        attributes.add(new Attribute("healRate",0,100,50));
        attributes.add(new Attribute("siteRange",0,1000,800));
        attributes.add(new Attribute("childEnergy",0,100,50));
        attributes.add(new Attribute("childFat",0,100,50));
    }

    public int getAttribute(String name) {
        synchronized (attributes) {
            boolean found = false;
            for (Attribute a : attributes) {
                if (a.name.equals(name)) {
                    found = true;
                    return a.value;
                }
            }
            if(!found) {
                System.out.println("Failed to find DNA attribute " + name);
                System.exit(-1);
            }
            return -100;
        }
    }

    public void setAttribute(String name, int value) {
        synchronized (attributes) {
            boolean found = false;
            for (Attribute a : attributes) {
                if (a.name.equals(name)) {
                    found = true;
                    a.value = value;
                }
            }
            if(!found) {
                System.out.println("Failed to set DNA attribute " + name );
                System.exit(-1);
            }
        }
    }

    public int getNormAttribute(String name) {
        synchronized (attributes) {
            boolean found = false;
            for (Attribute a : attributes) {
                if (a.name.equals(name)) {
                    found = true;
                    int x = a.max - a.min;
                    float y = (float)a.value / (float)x;
                    int z = (int)(y * 100);
                    int normalized = z;
                    return normalized;
                }
            }
            if(!found) {
                System.out.println("Failed to find DNA attribute " + name);
                System.exit(-1);
            }
            return -100;
        }
    }

    public int[] getAttributes() {
        synchronized (attributes) {
            int[] result = new int[attributes.size()];
            for (int i = 0; i < attributes.size(); i++) {
                result[i] = attributes.get(i).value;
            }
            return result;
        }
    }

    public int[] getNormAttributes() {
        synchronized (attributes) {
            int[] result = new int[attributes.size()];
            for (int i = 0; i < attributes.size(); i++) {
                Attribute a = attributes.get(i);
                int x = a.max - a.min;
                float y = (float)a.value / (float)x;
                int z = (int)(y * 100);
                int normalized = z;
                result[i] = normalized;
            }
            return result;
        }
    }

    public String[] getNames() {
        synchronized (attributes) {
            String[] result = new String[attributes.size()];
            for (int i = 0; i < attributes.size(); i++) {
                result[i] = attributes.get(i).name;
            }
            return result;
        }
    }

    public int[][] getMinMax() {
        synchronized (attributes) {
            int[][] result = new int[2][attributes.size()];
            for (int i = 0; i < attributes.size(); i++) {
                Attribute a = attributes.get(i);
                int normalized = a.value / (a.max - a.min) * 100;
                result[0][i] = a.min;
                result[1][i] = a.max;
            }
            return result;
        }
    }

    public void setAttributes(int[] newAttributes) {
        synchronized (attributes) {
            if (newAttributes.length == attributes.size()) {
                for (int i = 0; i < attributes.size(); i++) {
                    attributes.get(i).value = newAttributes[i];
                }
            }
        }
    }

    public DNA makeNew(DNA pDNA) {
        synchronized (attributes) {
            int[] tAtts = this.getAttributes();
            int[] pAtts = pDNA.getAttributes();
            int size = tAtts.length;

            if (pAtts.length == size) {
                int[] nAtts = new int[size];
                int cOPA = (int) (Math.random() * size);
                int cOPB = (int) (Math.random() * size);
                for (int i = 0; i < Math.min(cOPA, cOPB); i++) {
                    nAtts[i] = tAtts[i];
                }
                for (int i = Math.min(cOPA, cOPB); i < Math.max(cOPA, cOPB); i++) {
                    nAtts[i] = pAtts[i];
                }
                for (int i = Math.max(cOPA, cOPB); i < size; i++) {
                    nAtts[i] = tAtts[i];
                }
                DNA newDNA = new DNA();
                newDNA.setAttributes(nAtts);
                return newDNA;
            } else {
                return null;
            }
        }
    }

    public void mutate(int rate, int amount) {
        synchronized (attributes) {
            if (shouldLargeMutate(rate, amount)) {
                for (Attribute a : attributes) {
                    a.value = doMutate(a.value, a.min, a.max, 100, amount * 10);
                }
            } else {
                for (Attribute a : attributes) {
                    a.value = doMutate(a.value, a.min, a.max, rate, amount);
                }
            }
        }
    }

    public boolean shouldLargeMutate(int rate, int amount) {
        if (Math.random() * 100 < rate / 10) {
            return true;
        }
        return false;
    }

    public int doMutate(int value, int lower, int upper, int rate, int amount) {
        if (Math.random() * 100 < rate) {
            int mutation = (int) ((Math.random() * amount) - (amount / 2));
            value += mutation;
            if (value > upper) {
                value = upper;
            }
            if (value < lower) {
                value = lower;
            }
        }
        return value;
    }
}

class Attribute {

    public String name;
    public int min,  max,  value;

    public Attribute(String name, int min, int max, int value) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.value = value;
    }
}
