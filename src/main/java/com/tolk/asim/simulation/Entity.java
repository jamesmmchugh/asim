/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tolk.asim.simulation;

import java.awt.*;

/**
 *
 * @author James
 */
public class Entity {
    public boolean alive = true;
    protected SpriteAnim sprite;
    protected int frame =0;
    public int x = 0;
    public int y = 0;
    public World world;
    
    public Entity(String ref, int range, World w) {
        sprite = new SpriteAnim(SpriteStore.get().getSprite(ref, range, 0xFFFFFF));
        world = w;
    }
    
    public void setSprite(String ref, int frames, int colour) {
        sprite.setSprite(SpriteStore.get().getSprite(ref, frames, colour));
    }
    
    public boolean collidesWith(Entity other) {
        Rectangle me = new Rectangle();
        Rectangle him = new Rectangle();
        me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());

        return me.intersects(him);
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
