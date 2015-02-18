/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.logic;

import com.tolk.asim.simulation.Simulation;

/**
 *
 * @author jmmchugh
 */
public class DrawDispatcher {
    //GUI
    SimPanel simPanel;
    
    public DrawDispatcher(SimPanel simPanel)
    {
        this.simPanel = simPanel;
    }
    
    public void draw(Simulation sim)
    {
        //Anything else that needs to be done before, goes here
        simPanel.repaint();
    }

    public SimPanel getSimPanel() {
        return simPanel;
    }

    public void setSimPanel(SimPanel simPanel) {
        this.simPanel = simPanel;
    }
    
    
}
