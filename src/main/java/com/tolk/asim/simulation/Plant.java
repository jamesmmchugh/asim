/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

import java.awt.*;

import com.tolk.asim.logic.SimPanel;

/**
 *
 * @author James
 */
public class Plant extends Entity {

    public static float PlantsPerSecond = 0;

    public static final int START_ENERGY = 1000;
    public static final int AVERAGE_AGE = 400;
    public static final int AGE_VARIATION = 50;
    public static final int PLANT_SPREAD = 1000;
    public static final int MAXIMUM_PLANTS = 1000;
    private int energy;
    private int life;
    private int age;
    private boolean dieing = false;
    private Entity engaged = null;

    public Plant(String ref, int range, World w, boolean randomAge) {
        super(ref, range, w);
        //Setup attributes
        energy = START_ENERGY; //Energy set to start energy
        life = AVERAGE_AGE - (int) (Math.random() * (double) (2 * AGE_VARIATION) - (double) AGE_VARIATION); //Life set to average +/- variation
        if(randomAge) {
            age = (int)(Math.random() * Plant.AVERAGE_AGE);
        } else {
            age = 0; //Start age is zero
        }
    }

    public int getEnergy() {
        return this.energy;
    }

    public Entity isInUse() {
        return this.engaged;
    }

    public void use(Entity engaged) {
        this.engaged = engaged;
    }

    public void setPosition(int x, int y) {
        world.updateEntityPosition(this, x, y);
    }

    public void update(int no) {
        engaged = null;
        age(no);
    }

    public void age(int no) {
        age++;
        if (dieing) {
            energy--;
            if (energy <= 0) {
                dead();
            }
        } else {
            if (age >= life) {
                dieing = true;
            }
        }
        if (no == 0 && !world.getProperties().isConstantPlants()) {
            /*if (age % world.getSimulation().getPlantGrowthRate() == 0) {
                reproduce();
            }*/
            float x = (float)world.getProperties().getPlantGrowthRate() / 100;
            this.PlantsPerSecond += x;
            //System.out.println(this.PlantsPerSecond);
            if((int)this.PlantsPerSecond > 0) {
                System.out.println("REPRODUCING");
                for(int i=0; i<(int)this.PlantsPerSecond; i++) {
                    reproduce();
                }
                this.PlantsPerSecond = 0;
            }
        }
    }
    

    public int eat(int amount) {
        energy -= amount;
        if (energy >= 0) {
            return amount;
        } else {
            //Plant is consumed
            amount += energy;
            energy = 0;
            dieing = true;
            return amount;
        }
    }

    public void dead() {
        energy = 0;
        if (world.getProperties().isConstantPlants()) {
            //CREATE A NEW PLANT TO REPLACE OLD ONE
            reproduce();
        }
        //DESTROY THIS PLANT
        world.removeOldEntity(this);
    }

    public void reproduce() {
        if (world.getNoPlants() < MAXIMUM_PLANTS) {
            Plant p = new Plant("sprites/plant.png", 1, world, false);
            if (world.getProperties().isUniformPlants()) {
                //CREATE NEW PLANT IN A RANDOM POSITION
                p.x = (int) (Math.random() * world.getSize());
                p.y = (int) (Math.random() * world.getSize());
                world.addNewEntity(p);
            } else {
                //CREATE NEW PLANT IN LOCALLY RANDOM POSITION
                Plant parent = world.getRandomPlant();
                int newX = nextInt(parent.getX() - PLANT_SPREAD, x + PLANT_SPREAD);
                int newY = nextInt(parent.getY() - PLANT_SPREAD, y + PLANT_SPREAD);
                if (newX < 0) {
                    newX = world.getSize();
                }
                if (newX > world.getSize()) {
                    newX = 0;
                }
                if (newY < 0) {
                    newY = world.getSize();
                }
                if (newY > world.getSize()) {
                    newY = 0;
                }
                p.x = newX;
                p.y = newY;
                world.addNewEntity(p);
            }
        }
    }

    public void draw(SimPanel simPanel) {
        simPanel.draw(sprite.getFrame(), this.x, this.y, Color.GREEN);
    }

    public static int nextInt(int low, int high) {
        return Math.min(low, high) + (int) (Math.random() * (Math.abs(high - low)));
    }
}
