package com.tolk.asim.listener;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jmmchugh
 */
public interface SimulationListener {
    public void populationUpdateEvent(SimulationEvent e);
    public void geneticsUpdateEvent(SimulationEvent e);
    public void generalSimulationEvent(SimulationEvent e);
}
