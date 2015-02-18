package com.tolk.asim;

import javax.swing.*;

import com.tolk.asim.gui.IntroFrame;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

/**
 *
 * @author jmmchugh
 */
public class Main {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
                setLookAtFeel();
                new IntroFrame(false).setVisible(true);
			}
		});
	}

    private static void setLookAtFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Could not set look and feel");
        }
    }
}
