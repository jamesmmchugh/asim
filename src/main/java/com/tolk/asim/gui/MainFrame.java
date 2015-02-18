package com.tolk.asim.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.tolk.asim.listener.SimulationEvent;
import com.tolk.asim.listener.SimulationListener;
import com.tolk.asim.logic.DrawDispatcher;
import com.tolk.asim.logic.SimPanel;
import com.tolk.asim.logic.SimulationRunnable;
import com.tolk.asim.simulation.Simulation;

public class MainFrame extends JFrame implements ActionListener, SimulationListener {

    private Dimension gamePanelSize = new Dimension(800, 600);
    private Dimension optionPanelSize = new Dimension(310, gamePanelSize.height);
    private Dimension chartPanelSize = new Dimension(gamePanelSize.width + optionPanelSize.width, 360);
    private Dimension totalSize = new Dimension(chartPanelSize.width, gamePanelSize.height + chartPanelSize.height);
    private NewSpeciesFrame newSpeciesFrame;
    private NewSimulationFrame newSimulationFrame;
    private SimPanel gamePanel;
    private SimulationRunnable simulationRunnable;
    private JScrollPane scrollPane;
    private MainOptionPanel optionPanel;
    private MainMenuBar menubar;

    public MainFrame(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(totalSize);
        this.setSize(totalSize);
        this.setMinimumSize(new Dimension((int) optionPanelSize.getWidth(), (int) optionPanelSize.getWidth()));
        
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        //Init gamePanel and option frames
        
        Simulation tempSim = new Simulation(8000, false, true, 0, 10, 50);
        gamePanel = new SimPanel(tempSim, scrollPane.getViewport());
        DrawDispatcher d = new DrawDispatcher(gamePanel);
        simulationRunnable = new SimulationRunnable(tempSim, d);
        simulationRunnable.addEventListener(this);
        
        scrollPane.setViewportView(gamePanel);
        
        newSpeciesFrame = new NewSpeciesFrame("New Species", this);
        newSimulationFrame = new NewSimulationFrame("New Simulation", this);

        //Create scrollpane and add game window
        //scrollPane = new JScrollPane(gamePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scrollPane.setMinimumSize(gamePanelSize);
        //scrollPane.setPreferredSize(new Dimension(32767, 32767));
        //scrollPane.setWheelScrollingEnabled(false);

        //Create options panel
        optionPanel = new MainOptionPanel(this, optionPanelSize);

        //Create menubar and menus
        menubar = new MainMenuBar(this);

        //Add the interface items
        this.setMenuBar(menubar);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(optionPanel, BorderLayout.EAST);

        //Pack the application and set as visible
        this.pack();
        this.setVisible(true);

        simulationRunnable.start();
    }

    public void exitProgram() {
        System.exit(0);
    }

    public void updateControlSettings() {
    }

    public void newSimulation() {
        int worldSize = (Integer) this.newSimulationFrame.sizeSpinner.getValue();
        this.simulationRunnable.setPaused(true);
        synchronized (this.simulationRunnable.getSim()) {
            this.simulationRunnable.setSim(new Simulation(worldSize, false, true, 0, 10, 50));
            this.gamePanel.setPreferredSize(new Dimension(worldSize, worldSize));
            this.optionPanel.sendSettings();
        }
        newSimulationFrame.setVisible(false);
    }

    public void setSimulationSettings(boolean play, int speed, double zoom, boolean drawn, int pRate, boolean pDist, boolean pConst, int mRate, int mDegree) {
        simulationRunnable.setPaused(!play);
        simulationRunnable.getSim().getProperties().setSpeed(speed);
        gamePanel.reScale(zoom);
        simulationRunnable.getDispatcher().getSimPanel().setDraw(drawn);
        simulationRunnable.getSim().getWorld().getProperties().setPlantGrowthRate(pRate);
        simulationRunnable.getSim().getWorld().getProperties().setUniformPlants(pDist);
        simulationRunnable.getSim().getWorld().getProperties().setConstantPlants(pConst);
        simulationRunnable.getSim().setMutationRate(mRate);
        simulationRunnable.getSim().setMutationAmount(mDegree);
    //this.validate();
    }

    public void actionPerformed(ActionEvent e) {
        String commandArray[] = e.getActionCommand().split(":");
        String command = commandArray[0];
        if (command.equals("exit")) {
            exitProgram();
        } else if (command.equals("newSpecies")) {
            System.out.println("New Species Requested");
            newSpeciesFrame.setVisible(true);
        } else if (command.equals("newSimulation")) {
            System.out.println("New Simulation Requested");
            newSimulationFrame.setVisible(true);
        } else if (command.equals("newSimulationConfirm")) {
            System.out.println("New Simulation Confirmed");
            newSimulation();
        } else if (command.equals("manual")) {
            Point p = getLocation();
            new TextFileDialog(this, "Game of Life Manual", "strings/manual.txt", p.x + 60, p.y + 60);
        } else if (command.equals("about")) {
            IntroFrame about = new IntroFrame(true);
            about.setVisible(true);
        } else {
            System.out.println("Unrecognised Action");
        }
    }

    public SimPanel getGamePanel() {
        return this.simulationRunnable.getDispatcher().getSimPanel();
    }
    
    public SimulationRunnable getSimulationRunnable()
    {
        return this.simulationRunnable;
    }

    @Override
    public void generalSimulationEvent(SimulationEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void geneticsUpdateEvent(SimulationEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void populationUpdateEvent(SimulationEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}