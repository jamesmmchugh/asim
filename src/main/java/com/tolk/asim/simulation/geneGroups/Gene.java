/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tolk.asim.simulation.geneGroups;

import com.tolk.asim.simulation.CACTION;
import com.tolk.asim.simulation.Creature;

/**
 *
 * @author James
 */
public interface Gene {
    public int getDesire(Creature c);
    public CACTION chooseAction(Creature c);
}
