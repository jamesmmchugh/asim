package com.tolk.asim.simulation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A sprite to be displayed on the screen. Note that a sprite
 * contains no state information, i.e. its just the image and 
 * not the location. This allows us to use a single sprite in
 * lots of different places without having to store multiple 
 * copies of the image.
 * 
 * @author James McHugh
 */
public class Sprite {

    /** The image to be drawn for this sprite */
    //protected Image[] frame;
    protected ArrayList<BufferedImage> frames = new ArrayList();

    /**
     * Create a new sprite based on an image
     * 
     * @param image The image that is this sprite
     */
    public Sprite(BufferedImage image) {
        this.frames.add(image);
    }

    public void addFrame(BufferedImage image) {
        this.frames.add(image);
    }

    public int frames() {
        return frames.size();
    }

    public int getWidth(int frame) {
        return frames.get(frame).getWidth(null);
    }

    public int getHeight(int frame) {
        return frames.get(frame).getHeight(null);
    }
    
    public BufferedImage getImage(int frame) {
        return frames.get(frame);
    }
    /**
     * Draw the sprite onto the graphics context provided
     * 
     * @param g The graphics context on which to draw the sprite
     * @param x The x location at which to draw the sprite
     * @param y The y location at which to draw the sprite
     */
    /*public void draw(Graphics2D g,int x,int y) {
    g.drawImage(frames.get(currentFrame),x,y,null);
    }*/
}