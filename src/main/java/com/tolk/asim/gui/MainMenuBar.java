/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tolk.asim.gui;

import java.awt.*;

/**
 *
 * @author James
 */
public class MainMenuBar extends MenuBar {
    private MainFrame gui;
    public MainMenuBar(MainFrame gui) {
        
        super();
        this.gui = gui;
        
        Menu fileMenu = new Menu("File", true);
        //<editor-fold defaultstate="collapsed" desc="File Menu Code">
        /*MenuItem readMenuItem = new MenuItem("Open...");
        readMenuItem.setActionCommand("open");
        readMenuItem.addActionListener(gui);
        MenuItem writeMenuItem = new MenuItem("Save...");
        writeMenuItem.addActionListener(gui);
        writeMenuItem.setActionCommand("save");*/
        MenuItem quitMenuItem = new MenuItem("Exit");
        quitMenuItem.setActionCommand("exit");
        quitMenuItem.addActionListener(gui);

        Menu newMenu = new Menu("New", true);
        MenuItem newSimulationItem = new MenuItem("Simulation");
        newSimulationItem.setActionCommand("newSimulation");
        newSimulationItem.addActionListener(gui);
        MenuItem newSpeciesItem = new MenuItem("Species");
        newSpeciesItem.setActionCommand("newSpecies");
        newSpeciesItem.addActionListener(gui);
        
        newMenu.add(newSimulationItem);
        newMenu.add(newSpeciesItem);
        // </editor-fold>
        fileMenu.add(newMenu);
        /*fileMenu.add(readMenuItem);
        fileMenu.add(writeMenuItem);*/
        fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);
        
        Menu helpMenu = new Menu("Help", true);
        // <editor-fold defaultstate="collapsed" desc="Help Menu Code">

        MenuItem manualMenuItem = new MenuItem("Manual");
        manualMenuItem.setActionCommand("manual");
        manualMenuItem.addActionListener(gui);
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setActionCommand("about");
        aboutMenuItem.addActionListener(gui);
        // </editor-fold>
        helpMenu.add(manualMenuItem);
        helpMenu.add(aboutMenuItem);
        
        this.add(fileMenu);
        this.add(helpMenu);
    }

}
