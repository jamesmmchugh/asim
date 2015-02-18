/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.data;

/**
 *
 * @author jmmchugh
 */
public class WorldProperties {
    int plantGrowthRate;
    boolean uniformPlants;
    boolean constantsPlants;

    public WorldProperties(int plantGrowthRate, boolean uniformPlants, boolean constantsPlants) {
        this.plantGrowthRate = plantGrowthRate;
        this.uniformPlants = uniformPlants;
        this.constantsPlants = constantsPlants;
    }
    
    public boolean isConstantPlants() {
        return constantsPlants;
    }

    public void setConstantPlants(boolean constantsPlants) {
        this.constantsPlants = constantsPlants;
    }

    public int getPlantGrowthRate() {
        return plantGrowthRate;
    }

    public void setPlantGrowthRate(int plantGrowthRate) {
        this.plantGrowthRate = plantGrowthRate;
    }

    public boolean isUniformPlants() {
        return uniformPlants;
    }

    public void setUniformPlants(boolean uniformPlants) {
        this.uniformPlants = uniformPlants;
    } 
}
