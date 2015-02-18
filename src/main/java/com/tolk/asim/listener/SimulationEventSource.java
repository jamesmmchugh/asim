package com.tolk.asim.listener;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jmmchugh
 */
public abstract class SimulationEventSource {
    private List<SimulationListener> _listeners = new ArrayList<SimulationListener>();
    
    public synchronized void addEventListener(SimulationListener listener)
    {
        _listeners.add(listener);
    }
    
    public synchronized void removeEventListener(SimulationListener listener)
    {
        _listeners.remove(listener);
    }
    
    private synchronized void speciesPopulationEvent()
    {
        SimulationEvent e = new SimulationEvent(this, 1, null, new EventData());
        for(SimulationListener l : _listeners)
        {
            l.populationUpdateEvent(e);
        }
    }
    private synchronized void speciesGeneticEvent()
    {
        SimulationEvent e = new SimulationEvent(this, 1, null, new EventData());
        for(SimulationListener l : _listeners)
        {
            l.geneticsUpdateEvent(e);
        }
    }
    private synchronized void generalSimulationEvent()
    {
        SimulationEvent e = new SimulationEvent(this, 1, null, new EventData());
        for(SimulationListener l : _listeners)
        {
            l.generalSimulationEvent(e);
        }
    }
}
