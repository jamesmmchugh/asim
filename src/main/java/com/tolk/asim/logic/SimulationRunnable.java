/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.logic;

import com.tolk.asim.listener.SimulationEventSource;
import com.tolk.asim.simulation.Simulation;

/**
 *
 * @author jmmchugh
 */
public class SimulationRunnable extends SimulationEventSource implements Runnable {
    
    //Simulation
    Simulation sim;
    
    //Runnable
    Thread thread;
    boolean running = true;
    boolean paused = false;
    private long period = 10;
    private static final int NO_DELAYS_PER_YIELD = 16;
    
    //Drawing
    DrawDispatcher dispatcher;
    
    public SimulationRunnable(Simulation sim, DrawDispatcher dispatcher)
    {
        this.sim = sim;
        this.dispatcher = dispatcher;
    }
    
    public void start() {
        if (thread == null || !running) {
            thread = new Thread(this);
            thread.start();
        }
    } 
    
    public void run() /* Repeatedly update, render, sleep so loop takes close
    to period nsecs. Sleep inaccuracies are handled.
    The timing calculation use the sytem nano timer.
     */ { 
        long timeLast, lastTime = 0, frameTime = 0;
        int frames = 0;
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;

        beforeTime = System.nanoTime();
        lastTime = System.nanoTime();
        
        setup();

        running = true;
        while (running) {
            
            timeLast = System.nanoTime() - lastTime;
            frameTime += timeLast;
            frames++;
            lastTime = System.nanoTime();
            
            if(!paused)
            {
                sim.update(timeLast, timeLast * sim.getProperties().getSpeed());
                //TODO FIRE_SIMULATION_EVENT_SPECIES_POPULATION;
                //TODO FIRE_SIMULATION_EVENT_SPECIES_DNA;
            }
            
            dispatcher.draw(sim);

            if (frameTime > Simulation.SPEED_CONSTANT * 1) {
                System.out.println("fps: " + frames);
                frameTime = 0;
                frames = 0;
            }

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;

            if (sleepTime > 0) {   // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L);  // nano -> ms
                } catch (InterruptedException ex) {
                }
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {    // sleepTime <= 0; frame took longer than the period
                overSleepTime = 0L;

                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();   // give another thread a chance to run
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();
        }
    }
    
    public void setup()
    {
        
    }

    public Simulation getSim() {
        return sim;
    }

    public void setSim(Simulation sim) {
        this.sim = sim;
    }

    public DrawDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(DrawDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    
    
}
