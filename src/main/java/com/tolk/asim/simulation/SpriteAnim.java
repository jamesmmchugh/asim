/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.simulation;

import java.awt.image.BufferedImage;

/**
 *
 * @author James
 */
public class SpriteAnim {

    protected Sprite sprite;
    protected int frame;
    
    public SpriteAnim(Sprite s) {
        sprite = s;
    }

    public void setSprite(Sprite s) {
        if(s != sprite) {
            sprite = s;
            frame = 0;
        }
    }

    public int getWidth() {
        return sprite.getWidth(frame);
    }

    public int getHeight() {
        return sprite.getHeight(frame);
    }
    
    public BufferedImage getFrame() {
        return sprite.getImage(frame);
    }

    public void nextFrame() {
        if (frame < sprite.frames() - 1) {
            frame++;
        } else {
            frame = 0;
        }
    }
    public int frames() {
        return sprite.frames();
    }
}
