/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.data;

/**
 *
 * @author jmmchugh
 */
public class SimulationProperties {
    int speed;

    public SimulationProperties(int speed) {
        this.speed = speed;
    }
    
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
