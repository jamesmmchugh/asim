/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

import java.awt.*;
import java.awt.geom.Point2D;

import com.tolk.asim.logic.SimPanel;
import com.tolk.asim.simulation.geneGroups.FeedingGene;
import com.tolk.asim.simulation.geneGroups.FleeingGene;
import com.tolk.asim.simulation.geneGroups.Gene;
import com.tolk.asim.simulation.geneGroups.MatingGene;

/**
 *
 * @author James
 */
public class Creature extends Entity {

    public static int creatures = 0;
    private int creatureID = 0;
    public static final int MAX_ENERGY = 200;
    public static final int MAX_FAT = 400;
    public static final int MAX_HEALTH = 100;
    public static final int AVERAGE_AGE = 900;
    public static final int AGE_VARIATION = 20;
    public static final int MAINTENANCE_COST = 4;
    public static final int MAX_CONSUME = 10;
    private int age = 0;
    private int life;
    private Species species;
    private Color colour;
    private Entity target;
    private int randX = -1;
    private int randY = -1;
    private long lastMove = 0;
    //Genes
    private FeedingGene gFood;
    private FleeingGene gFlee;
    private MatingGene gMate;
    private String status = "";
    //Static Properties
    private int generation;
    private DNA dna = new DNA();
    //Affected attributes
    private int health = 100;
    private int energy = 80;
    private int fat = 80;
    //Dependant attributes
    private int weight;
    private int attackDamage;
    private int movementSpeed;
    private CACTION creatureAction;
    private CSTATE creatureState;
    private Gene currentGene;
    private CACTION currentSprite;
    private boolean full = false;
    private int ageOfDeath = 0;
    private Entity killer = null;

    public Creature(Species species, String spriteRef, int spriteFrames, DNA dna, World w) {
        super(spriteRef, spriteFrames, w);
        Creature.creatures++;
        this.creatureID = Creature.creatures;
        this.species = species;
        this.colour = species.colour;
        this.dna = dna;
        this.life = AVERAGE_AGE - (int) (Math.random() * (double) (2 * AGE_VARIATION) - (double) AGE_VARIATION);
    }

    public boolean isFull() {
        return this.full;
    }

    public void filled(boolean full) {
        this.full = full;
    }

    public int getHealth() {
        return this.health;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getFat() {
        return this.fat;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public Entity getKiller() {
        return this.killer;
    }

    public CSTATE getState() {
        return this.creatureState;
    }

    public DNA getDNA() {
        return this.dna;
    }

    public void setDNA(DNA dna) {
        this.dna = dna;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Species getSpecies() {
        return this.species;
    }

    public void think() {
        age++;
        //creatureState = CSTATE.UNKNOWN;
        //creatureAction = CACTION.NONE;
        if (alive) {
            updateLevels();
            int scared = dna.scaredGene.getDesire(this);
            int horny = dna.hornyGene.getDesire(this);
            int hunger = dna.hungerGene.getDesire(this);
            if (scared >= horny && scared >= hunger) {
                creatureState = CSTATE.SCARED;
            }
            if (horny > scared && horny >= hunger) {
                creatureState = CSTATE.HORNY;
            }
            if (hunger > scared && hunger > horny) {
                creatureState = CSTATE.HUNGRY;
            }
            /*if(dna.hungerGene instanceof MeatNearFirst) {
            System.out.println(scared + " " + horny + " " + hunger);
            }*/
            chooseAction();
            checkFull();
            costTurn();
        }
    }

    public void chooseAction() {
        target = null;
        if (creatureState == CSTATE.SCARED) {
            creatureAction = dna.scaredGene.chooseAction(this);
            currentGene = dna.scaredGene;
        }
        if (creatureState == CSTATE.HORNY) {
            creatureAction = dna.hornyGene.chooseAction(this);
            currentGene = dna.hornyGene;
        }
        if (creatureState == CSTATE.HUNGRY) {
            creatureAction = dna.hungerGene.chooseAction(this);
            currentGene = dna.hungerGene;
        }
        if (creatureState == CSTATE.UNKNOWN) {
            creatureAction = CACTION.UNKOWN;
            currentGene = null;
        }
        validateTarget();
    }

    public void validateTarget() {
        if (creatureAction == CACTION.PASSIVESEARCH) {
            //creatureState = CSTATE.HUNGRY;
            creatureAction = dna.hungerGene.chooseAction(this);
            currentGene = dna.hungerGene;
        }
        if (target == null) {
            //creatureState = CSTATE.UNKNOWN;
            creatureAction = CACTION.UNKOWN;
            currentGene = null;
        }
        //System.out.println("Creature is " + creatureState + " and will be " + creatureAction + " target " + target);
        //System.out.println("Creatures stats - energy: " + energy + " fat: " + fat + " health: " + health);
        setSprite();
    }

    public void setSprite() {
        if (currentSprite != creatureAction) {
            currentSprite = creatureAction;
            frame = 0;
            if (creatureAction == CACTION.CHASING) {
                if (creatureState == CSTATE.HUNGRY) {
                    sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
                }
                if (creatureState == CSTATE.HORNY) {
                    sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureMateWalk*.png", 4, colour.getRGB()));
                }
                if (creatureState == CSTATE.SCARED) {
                    sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
                }
            }
            if (creatureAction == CACTION.EATING) {
                sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureEat*.png", 5, colour.getRGB()));
            }
            if (creatureAction == CACTION.FIGHTING) {
                sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
            }
            if (creatureAction == CACTION.FLEEING) {
                sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
            }
            if (creatureAction == CACTION.MATING) {
                sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
            }
            if (creatureAction == CACTION.NONE) {
                sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
            }
            if (creatureAction == CACTION.MATESEARCH) {
                sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
            }
            if (creatureAction == CACTION.UNKOWN) {
                sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureWalk*.png", 4, colour.getRGB()));
            }
        }
        if (creatureID == 1) {
            if (age == 1) {
                System.out.println("Speed " + getSpeed() + " " + dna.getAttribute("targetMinEnergy") + " " + dna.getAttribute("targetStoreEnergy") + " " + dna.getAttribute("strength") + " " + dna.getAttribute("metabolismRate"));
            }
            //System.out.println(this.getSpeed() + " " + this + "\t" + species.name + "\t" + health + "\t" + energy + "\t" + fat + "\t" + creatureState + "\t" + creatureAction + "\t" + full +"\t"+ target);
            System.out.println(health + " " + energy + " " + fat + " " + creatureState + " " + creatureAction);

        }
    }

    public void act(long delta) {
        if (age > life) {
            alive = false;
        }
        if (alive) {
            if (creatureAction == CACTION.CHASING) {
                moveTo(target, delta);
            }
            if (creatureAction == CACTION.EATING) {
                eat(target);
                creatureAction = CACTION.NONE;
            }
            if (creatureAction == CACTION.FIGHTING) {
                fight(target);
                creatureAction = CACTION.NONE;
            }
            if (creatureAction == CACTION.FLEEING) {
                moveFrom(target, delta);
            }
            if (creatureAction == CACTION.MATING) {
                //System.out.println("MATINGGGGGGG");
                mate(target);
                creatureAction = CACTION.NONE;
            }
            if (creatureAction == CACTION.MATESEARCH) {
                moveRandom(delta);
            }
            if (creatureAction == CACTION.UNKOWN) {
                moveRandom(delta);
            }
            if (creatureAction == CACTION.NONE) {
            }
        } else {
            creatureState = CSTATE.DEAD;
            creatureAction = CACTION.NONE;
            sprite.setSprite(SpriteStore.get().getSprite("sprites/creatureDead.png", 1, colour.getRGB()));
            if (ageOfDeath == 0) {
                ageOfDeath = age;
            } else {
                if (age > ageOfDeath + 30) {
                    world.removeOldEntity(this);
                }
            }
        }
    }

    public void costTurn() {
        int maintenanceDue = Creature.MAINTENANCE_COST;
        maintenanceDue -= this.changeEnergy(-maintenanceDue);
        if (maintenanceDue > 0) {
            maintenanceDue -= this.changeFat(-maintenanceDue * 2) / 2;
            if (maintenanceDue > 0) {
                maintenanceDue -= this.changeHealth(-maintenanceDue * 2) / 2;
            }
        }
    }

    public void heal() {
        if (this.health < MAX_HEALTH) {
            int canHeal = this.dna.getNormAttribute("healRate") / 10;
            int needs = MAX_HEALTH - this.health;
            if (needs < canHeal) {
                canHeal = needs;
            }
            int healed = this.changeEnergy(-canHeal);
            this.changeHealth(healed);
            canHeal -= healed;
            if (canHeal > 0) {
                healed = this.changeFat(-canHeal * 2) / 2;
                this.changeHealth(healed);
            }
        }
    }

    public void energise() {
        //If energy less than target min, then metabolise
        if (this.energy < this.dna.getAttribute("targetMinEnergy")) {
            int canMetabolise = this.dna.getNormAttribute("metabolismRate") / 10;
            this.changeEnergy(changeFat(-canMetabolise * 2) / 2);
        }
    }

    public void store() {
        int canMetabolise = this.dna.getNormAttribute("metabolismRate") / 10;
        //If energy greater than target level + amount needed to keep above, then store
        if(this.energy > this.getDNA().getAttribute("targetStoreEnergy") + canMetabolise && this.fat < Creature.MAX_FAT)
        {
            changeFat(changeEnergy(-canMetabolise * 2) / 2);
        }
    }

    public void checkFull() {
        if (energy < dna.getAttribute("targetMinEnergy") || fat < dna.getAttribute("targetMinFat")) {
            full = false;
        }
        if (energy >= MAX_ENERGY && fat >= MAX_FAT) {
            if (creatureID == 1) {
                System.out.println("I AM FULLLLLLLLLLLLLL");
            }
            full = true;
        }
    }

    public void updateLevels() {
        heal();
        energise();
        store();

        if (energy <= 0 || health == 0) {
            alive = false;
        }
    }

    public int getWeight() {
        //Theoretical range 0 - 100
        weight = (dna.getNormAttribute("strength") + dna.getNormAttribute("targetStoreEnergy")) / 2;
        return weight;
    }

    public int getSpeed() {
        float speed = (((float)dna.getNormAttribute("strength")-(float)dna.getNormAttribute("targetStoreEnergy"))+100)/2; //Strength to weight ratio
        speed += 60; //Speed range 40-140;
        movementSpeed = (int)speed;
        return movementSpeed;
    }

    public int getAttack() {
        //Theoretical range 0 - 100
        attackDamage = (dna.getNormAttribute("strength") + dna.getNormAttribute("targetMinEnergy")) / 2;
        return attackDamage;
    }

    public void eat(Entity e) {
        if (e instanceof Plant) {
            Plant p = (Plant) e;
            changeEnergy(p.eat(MAX_CONSUME));
        }
        if (e instanceof Creature) {
            Creature c = (Creature) e;
            int amount = 0;
            if (c.health > 0) {
                amount += c.changeHealth(-MAX_CONSUME);
            } else if (c.energy > 0) {
                amount += c.changeEnergy(-MAX_CONSUME);
            } else if (c.fat > 0) {
                amount += c.changeFat(-MAX_CONSUME);
            } else {
                world.removeOldEntity(e);
            }
            changeEnergy(amount);
        }
    }

    public void fight(Entity e) {
        if (e instanceof Creature) {
            Creature c = (Creature) e;
            int a = c.changeHealth(-this.getAttack());
            if (!c.alive) {
                c.killer = this;
            }
        //System.out.println(this + " hit creature " + c + " for " + a);
        }
    }

    public void mate(Entity e) {
        if (e instanceof Creature) {
            Creature child = world.getEntityCreator().newCreature("sprites/creatureWalk*.png", 4, this, (Creature) e);
            world.addNewEntity(child);
            //Creature child = world.getSimulation().getGenetics().newCreature("sprites/creatureWalk*.png", 4, world, this, (Creature) e);
            world.addNewEntity(child);
        }
    }

    public void moveTo(Entity e, long delta) {
        moveTo(e.x, e.y, delta);
    }

    public void moveFrom(Entity e, long delta) {
        moveFrom(e.x, e.y, delta);
    }

    public void moveRandom(long delta) {
        if ((x == randX && y == randY) || (randX == -1 && randY == -1)) {
            randX = (int) (Math.random() * world.getSize());
            randY = (int) (Math.random() * world.getSize());
        } else {
            moveTo(randX, randY, delta);
        }
    }

    public void moveFrom(int x, int y, long delta) {
        if (this.x == x && this.y == y) {
            x += (int) (Math.random() * 4);
            y += (int) (Math.random() * 4);
        }
        lastMove += delta;
        if (this.x != x | this.y != y) {
            int xVector = -this.x + x;
            int yVector = -this.y + y;
            double targetDistance = this.getDistance(x, y);
            double moveDistance = (double) (getSpeed()) * ((double) (lastMove) / 1000000000);
            if (moveDistance > targetDistance) {
                this.setPosition(x, y);
                lastMove = 0;
            } else {
                double distanceFraction = targetDistance / moveDistance;
                double moveX = (xVector / distanceFraction);
                double moveY = (yVector / distanceFraction);
                int mX = (int) moveX;
                int mY = (int) moveY;

                if (mX > 0 || mY > 0) {
                    this.setPosition((int) (this.x - mX), (int) (this.y - mY));
                    lastMove = 0;
                }
            }
        }
    }

    public void moveTo(int x, int y, long delta) {
        lastMove += delta;
        if (this.x != x | this.y != y) {
            int xVector = -this.x + x;
            int yVector = -this.y + y;
            double targetDistance = this.getDistance(x, y);
            double moveDistance = (double) (getSpeed()) * ((double) (lastMove) / 1000000000);
            if (moveDistance > targetDistance) {
                this.setPosition(x, y);
                lastMove = 0;
            } else {
                double distanceFraction = targetDistance / moveDistance;
                double moveX = (xVector / distanceFraction);
                double moveY = (yVector / distanceFraction);
                int mX = (int) moveX;
                int mY = (int) moveY;

                if (Math.abs(mX) > 0 || Math.abs(mY) > 0) {
                    this.setPosition((int) (this.x + mX), (int) (this.y + mY));
                    lastMove = 0;
                }
            }
        }
    }

    public void setPosition(int x, int y) {
        world.updateEntityPosition(this, x, y);
    }

    public int changeEnergy(int amount) {
        int returns = -amount;
        if (amount < 0) {
            this.energy += amount;
            if (energy < 0) {
                returns += energy;
                energy = 0;
            }
        } else if (amount > 0) {
            energy += amount;
            if (energy > MAX_ENERGY) {
                energy = MAX_ENERGY;
            }
        }
        return returns;
    }

    public int changeFat(int amount) {
        int returns = -amount;
        if (amount < 0) {
            fat += amount;
            if (fat < 0) {
                returns += fat;
                fat = 0;
            }
        } else if (amount > 0) {
            fat += amount;
            if (fat > MAX_FAT) {
                fat = MAX_FAT;
            }
        }
        return returns;
    }

    public int changeHealth(int amount) {
        int returns = -amount;
        if (amount < 0) {
            health += amount;
            if (health < 0) {
                returns += health;
                health = 0;
            }
        } else if (amount > 0) {
            health += amount;
            if (health > MAX_HEALTH) {
                health = MAX_HEALTH;
            }
        }
        if (health == 0) {
            alive = false;
        } else {
            alive = true;
        }
        return returns;
    }

    public double getDistance(Entity e) {
        Point2D targetEntity = new Point(e.x, e.y);
        Point2D thisEntity = new Point(this.x, this.y);
        return thisEntity.distance(targetEntity);
    }

    public double getDistance(int x, int y) {
        Point2D targetEntity = new Point(x, y);
        Point2D thisEntity = new Point(this.x, this.y);
        return thisEntity.distance(targetEntity);
    }

    public void updateGraphics() {
        frame++;
        sprite.nextFrame();
    }

    public void draw(SimPanel simPanel) {
        simPanel.draw(sprite.getFrame(), this.x, this.y, colour);
    }
}
