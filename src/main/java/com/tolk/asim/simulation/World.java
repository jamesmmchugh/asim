/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

import java.awt.*;
import java.util.ArrayList;

import com.tolk.asim.logic.SimPanel;
import com.tolk.asim.data.WorldProperties;

/**
 *
 * @author James
 */
public class World {
    //TO INIT

    private EntityCreator entityCreator;
    private WorldProperties properties;
    private int size;
    private int cellSize;
    private Simulation s;
    private long lastLogic, lastAnim;
    private ArrayList<Creature> creatures;
    private ArrayList<Plant> plants;
    private ArrayList<Species> species;
    private ArrayList<Entity> newEntityList;
    private ArrayList<Entity> oldEntityList;
    private ArrayList[][] creatureSectors;
    private ArrayList[][] plantSectors;
    private int seconds = 0;

    public World(Simulation s, int size, int cellSize, int plantGrowthRate, boolean unifromPlants, boolean constantsPlants) {
        this.entityCreator = new EntityCreator();
        this.properties = new WorldProperties(plantGrowthRate, unifromPlants, constantsPlants);

        this.s = s;
        this.size = size;
        this.cellSize = size;
        creatures = new ArrayList();
        plants = new ArrayList();
        species = new ArrayList();
        newEntityList = new ArrayList();
        oldEntityList = new ArrayList();

        int cells = size / cellSize;

        creatureSectors = new ArrayList[cells + 1][cells + 1];
        plantSectors = new ArrayList[cells + 1][cells + 1];

        for (int i = 0; i <= cells; i++) {
            for (int j = 0; j <= cells; j++) {
                creatureSectors[i][j] = new ArrayList();
                plantSectors[i][j] = new ArrayList();
            }
        }
    }

    public ArrayList getCreatureCell(int row, int col) {
        return this.creatureSectors[row][col];
    }

    public ArrayList getPlantCell(int row, int col) {
        return this.plantSectors[row][col];
    }

    public Point checkBounds(int x, int y) {
        if (x > this.getSize()) {
            x = this.getSize();
        } else if (x < 0) {
            x = 0;
        }
        if (y > this.getSize()) {
            y = this.getSize();
        } else if (y < 0) {
            y = 0;
        }
        return new Point(x, y);
    }

    public void updateEntityPosition(Entity e, int x, int y) {
        Point checked = checkBounds(x, y);
        if (e instanceof Creature) {
            synchronized (this.creatureSectors) {
                this.creatureSectors[e.getX() / this.getCellSize()][e.getY() / this.getCellSize()].remove(e);
                e.setXY(checked.x, checked.y);
                this.creatureSectors[checked.x / this.getCellSize()][checked.y / this.getCellSize()].add(e);
            }
        }
        if (e instanceof Plant) {
            synchronized (this.plantSectors) {
                this.plantSectors[e.getX() / this.getCellSize()][e.getY() / this.getCellSize()].remove(e);
                e.setXY(checked.x, checked.y);
                this.plantSectors[checked.x / this.getCellSize()][checked.y / this.getCellSize()].add(e);
            }
        }
    }

    public int getTime() {
        return this.seconds;
    }

    public void removeOldEntity(Entity e) {
        synchronized (this.oldEntityList) {
            this.oldEntityList.add(e);
        }
    }

    public void addNewEntity(Entity e) {
        synchronized (this.newEntityList) {
            this.newEntityList.add(e);
        }
    }

    public void addSpecies(Species s) {
        synchronized (this.species) {
            this.species.add(s);
        }
    }

    public void addPlant(Plant p) {
        synchronized (this.plants) {
            this.plants.add(p);
        }
    }

    public int getNoPlants() {
        return this.plants.size();
    }

    public Simulation getSimulation() {
        return this.s;
    }

    private void removeEntity(Entity e) {
        if (e instanceof Plant) {
            synchronized (plants) {
                plants.remove((Plant) e);
                plantSectors[(int) e.x / this.getCellSize()][(int) e.y / this.getCellSize()].remove(e);
            }
        } else if (e instanceof Creature) {
            synchronized (creatures) {
                removeFromSpeciesList((Creature) e);
                creatures.remove((Creature) e);
                creatureSectors[(int) e.x / this.getCellSize()][(int) e.y / this.getCellSize()].remove(e);
            }
        } else {
            System.out.println("Old entity invalid");
        }
    }

    private void addEntity(Entity e) {
        if (e instanceof Plant) {
            if (!plants.contains(e)) {
                plants.add((Plant) e);
                if (!plantSectors[(int) e.x / this.getCellSize()][(int) e.y / this.getCellSize()].contains(e)) {
                    plantSectors[(int) e.x / this.getCellSize()][(int) e.y / this.getCellSize()].add(e);
                }
            }
        } else if (e instanceof Creature) {
            addToSpeciesList((Creature) e);
            if (!creatures.contains(e)) {
                creatures.add((Creature) e);
                if (!creatureSectors[(int) e.x / this.getCellSize()][(int) e.y / this.getCellSize()].contains(e)) {
                    System.out.println((int) e.x / this.getCellSize() + " sdfsdf " + (int) e.y / this.getCellSize());
                    creatureSectors[(int) e.x / this.getCellSize()][(int) e.y / this.getCellSize()].add(e);
                }
            }
        } else {
            System.out.println("New entity invalid");
        }
    }

    private void addToSpeciesList(Creature c) {
        boolean found = false;
        c.getSpecies().addCreature(c);
    }

    private void removeFromSpeciesList(Creature c) {
        c.getSpecies().removeCreature(c);
    }

    public Species getSpecies(String ref) {
        Species s = null;
        for (Species search : species) {
            if (search.name.equals(ref)) {
                return search;
            }
        }
        return s;
    }

    private void newSpecies(int number, Object stats) {
        Species s = new Species(this, "null");
    }

    public void draw(SimPanel simPanel) {
        synchronized (creatures) {
            for (Creature c : creatures) {
                c.draw(simPanel);
            }
        }
        synchronized (plants) {
            for (Plant p : plants) {
                p.draw(simPanel);
            }
        }
    }

    public Plant getRandomPlant() {
        int index = (int) (Math.random() * plants.size());
        return plants.get(index);
    }

    public void update(long lastTime, long simLastTime) {
            synchronized (creatures) {
                synchronized (plants) {

                    lastLogic += simLastTime;
                    lastAnim += simLastTime;

                    synchronized (newEntityList) {
                        for (Entity e : newEntityList) {
                            addEntity(e);
                        }
                        newEntityList.clear();
                    }
                    synchronized (oldEntityList) {
                        for (Entity e : oldEntityList) {
                            removeEntity(e);
                        }
                        oldEntityList.clear();
                    }
                    if (lastLogic >= Simulation.SPEED_CONSTANT * 1) {
                        seconds++;
                        for (Species s : species) {
                            //System.out.println(s.name + " population: " + s.population());
                        }
                        for (int i = 0; i < plants.size(); i++) {
                            Plant p = plants.get(i);
                            p.update(i);
                        }
                        for (Creature c : creatures) {
                            c.think();
                        }
                        lastLogic = 0;
                    }


                    if (lastAnim >= Simulation.SPEED_CONSTANT * 0.1) {
                        for (Creature c : creatures) {
                            c.updateGraphics();
                        }
                        lastAnim = 0;
                    }

                    for (Creature c : creatures) {
                        c.act(simLastTime);
                    }
                    for (Plant p : plants) {
                        //TODO implement plant logic
                    }
                }
            }
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public EntityCreator getEntityCreator() {
        return entityCreator;
    }

    public void setEntityCreator(EntityCreator entityCreator) {
        this.entityCreator = entityCreator;
    }

    public WorldProperties getProperties() {
        return properties;
    }

    public void setProperties(WorldProperties properties) {
        this.properties = properties;
    }
}
