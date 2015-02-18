/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

/**
 *
 * @author James
 */
public class EntityCreator {

    public EntityCreator() { 
    }
    
    public void newCreatures(int number, String speciesRef, String spriteRef, int spriteFrames, DNA dna, World w) {
        for(int i=0; i<number; i++) {
            newCreature(speciesRef,spriteRef,spriteFrames,dna,w);
        }
    }
    
    public Creature newCreature(String speciesRef, String spriteRef, int spriteFrames, DNA dna, World w) {
        Species s = null;
        if(w.getSpecies(speciesRef)==null) {
            s = new Species(w, speciesRef, dna);
        } else {
            s = w.getSpecies(speciesRef);
        }
        Creature c = new Creature(s, spriteRef,spriteFrames, dna, w);
        int x = (int)(Math.random() * w.getSize());
        int y = (int)(Math.random() * w.getSize());

        c.setEnergy((int)(Math.random()*(Creature.MAX_ENERGY-c.getDNA().getAttribute("targetMinEnergy")))+c.getDNA().getAttribute("targetMinEnergy"));
        c.setFat((int)(Math.random()*(Creature.MAX_FAT-c.getDNA().getAttribute("targetMinFat")))+c.getDNA().getAttribute("targetMinFat"));
        c.setPosition(x,y);
        c.getDNA().mutate(w.getSimulation().getMutationRate(), w.getSimulation().getMutationAmount());
        w.addNewEntity(c);
        return c;
    }

    public Creature newCreature(String ref, int range, Creature mother, Creature father) {
        
        DNA newDNA = mother.getDNA().makeNew(father.getDNA());
        //TODO make references to mother.world into mother.getWorld()
        newDNA.mutate(mother.world.getSimulation().getMutationRate(), mother.world.getSimulation().getMutationAmount());
        newDNA.hornyGene = mother.getDNA().hornyGene;
        newDNA.hungerGene = mother.getDNA().hungerGene;
        newDNA.scaredGene = mother.getDNA().scaredGene;
        
        Creature child = new Creature(mother.getSpecies(), ref, range, newDNA, mother.world);
        child.setEnergy((int)(mother.getDNA().getAttribute("childEnergy")*((double)mother.getEnergy()/100)));
        child.setFat((int)(mother.getDNA().getAttribute("childFat")*((double)mother.getFat()/100)));
        mother.changeEnergy(-(int)(mother.getDNA().getAttribute("childEnergy")*((double)mother.getEnergy()/100)));
        mother.changeFat(-(int)(mother.getDNA().getAttribute("childFat")*((double)mother.getFat()/100)));
        child.setPosition(mother.x, mother.y);
        
        return child;
    }
}
