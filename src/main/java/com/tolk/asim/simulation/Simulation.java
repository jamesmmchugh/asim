/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import com.tolk.asim.logic.SimPanel;
import com.tolk.asim.data.SimulationProperties;
import com.tolk.asim.simulation.geneGroups.gene.FleeNone;
import com.tolk.asim.simulation.geneGroups.gene.MeatNearFirst;

/**
 *
 * @author James
 */
public class Simulation implements ActionListener {
    public final static int SPEED_CONSTANT = 1000000000;
    
    public final static int DEFAULT_WORLD_CELL_SIZE = 1000;
    
    //Simulation properties
    SimulationProperties properties;
    private int mutationRate; //Amount of mutation to be applied on reproduction, if -1 then mutation amount is dependant on individual creature
    private int mutationAmount; //Amount by which the mutation drops
    private World world;

    Thread simulationThread;

    public Simulation(int size, boolean uniformPlants, boolean constantsPlants, int plantGrowthRate, int mutationRate, int mutationAmount) {
        this.properties = new SimulationProperties(1);
        this.mutationRate = mutationRate;
        this.mutationAmount = mutationAmount;
        this.world = new World(this, size, DEFAULT_WORLD_CELL_SIZE, plantGrowthRate, uniformPlants, constantsPlants);
        
        DNA dna1 = new DNA();
        DNA dna2 = new DNA();
        dna2.setAttribute("strength",90);
        dna2.hungerGene = new MeatNearFirst();
        dna2.scaredGene = new FleeNone();
        this.world.getEntityCreator().newCreatures(200, "Herbivore","sprites/creatureWalk*.png",4, dna1, world);
        this.world.getEntityCreator().newCreatures(10, "Carnivore","sprites/creatureWalk*.png",4, dna2, world);
        
        for(int i=0; i<300; i++ ) {
            Plant pl = new Plant("sprites/plant.png",1,world, true);
            int x = (int)(Math.random() * world.getSize());
            int y = (int)(Math.random() * world.getSize());
            pl.setPosition(x,y);
            world.addPlant(pl);
        }
    }

    public World getWorld() {
        return this.world;
    }
    public int getMutationAmount() {
        return this.mutationAmount;
    }
    public void setMutationAmount(int degree) {
        this.mutationAmount = degree;
    }
    public int getMutationRate() {
        return this.mutationRate;
    }
    public void setMutationRate(int rate) {
        this.mutationRate = rate;
    }
    
    public SimulationProperties getProperties()
    {
        return this.properties;
    }
    
    public void setProperties(SimulationProperties properties)
    {
        this.properties = properties;
    }

    public void update(long lastTime, long simLastTime) {
        world.update(lastTime, simLastTime);
    }
    public void draw(SimPanel simPanel) {
        world.draw(simPanel);
    }

    public void drawImage(SimPanel simPanel, BufferedImage image, int x, int y, Color colour) {
        simPanel.draw(image, x, y, colour);
    }
    
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if(actionCommand.equals("newSpecies")) {
            System.out.println("Creating new species");
        } else {
            System.out.println("Unrecognised Action");
        }
    }
}
