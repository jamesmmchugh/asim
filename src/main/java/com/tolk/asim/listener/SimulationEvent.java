package com.tolk.asim.listener;


import java.awt.event.ActionEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jmmchugh
 */
public class SimulationEvent extends ActionEvent {
    
    EventData data;

    public SimulationEvent(Object source, int id, String command, EventData data)
    {
        super(source, id, command);
        this.data = data;
    }
    
    public EventData getEventData()
    {
        return this.data;
    }
}
