package com.tolk.asim.simulation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * A resource manager for sprites in the game. Its often quite important
 * how and where you get your game resources from. In most cases
 * it makes sense to have a central resource loader that goes away, gets
 * your resources and caches them for future use.
 * <p>
 * [singleton]
 * <p>
 * @author Kevin Glass
 */
public class SpriteStore {

    /** The single instance of this class */
    private static SpriteStore single = new SpriteStore();

    /**
     * Get the single instance of this class 
     * 
     * @return The single instance of this class
     */
    public static SpriteStore get() {
        return single;
    }
    /** The cached sprite map, from reference to sprite instance */
    private HashMap sprites = new HashMap();

    /**
     * Retrieve a sprite from the store
     * 
     * @param ref The reference to the image to use for the sprite
     * @return A sprite instance containing an accelerate image of the request reference
     */
    public Sprite getSprite(String ref, int range, int colour) {
        // if we've already got the sprite in the cache
        // then just return the existing version
        if (sprites.get(ref + colour) != null) {
            return (Sprite) sprites.get(ref + colour);
        }

        String[] wild = ref.split("\\*");
        if (wild.length == 2) {
            BufferedImage[] sourceImages = new BufferedImage[range];
            URL[] urls = new URL[range];
            Sprite sprite = null;
            for (int i = 0; i < range; i++) {
                try {
                    urls[i] = this.getClass().getClassLoader().getResource(wild[0] + i + wild[1]);
                    System.out.println("Loading sprite file: " + wild[0] + i + wild[1]);
                    if (urls[i] == null) {
                        fail("Can't find ref: " + ref);
                    }
                    sourceImages[i] = ImageIO.read(urls[i]);
                } catch (IOException e) {
                    fail("Failed to load: " + ref);
                }
                GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
                BufferedImage image = gc.createCompatibleImage(sourceImages[i].getWidth(), sourceImages[i].getHeight(), Transparency.BITMASK);

                // draw our source image into the accelerated image
                image.getGraphics().drawImage(sourceImages[i], 0, 0, null);

                for (int k = 0; k < image.getWidth(); k++) {
                    for (int j = 0; j < image.getHeight(); j++) {
                        if (image.getRGB(k, j) == -16737793) {
                            image.setRGB(k, j, colour);
                        }
                    }
                }

                // create a sprite, add it the cache then return it
                if (sprite == null) {
                    sprite = new Sprite(image);
                //sprite = new Sprite(newImage);
                } else {
                    sprite.addFrame(image);
                //sprite.addFrame(newImage);
                }
            }
            sprites.put(ref + colour, sprite);
            return sprite;
        } else {
            BufferedImage sourceImage = null;

            try {
                // The ClassLoader.getResource() ensures we get the sprite
                // from the appropriate place, this helps with deploying the game
                // with things like webstart. You could equally do a file look
                // up here.
                URL url = this.getClass().getClassLoader().getResource(ref);
                System.out.println("Loading sprite file: " + ref);
                if (url == null) {
                    fail("Can't find ref: " + ref);
                }

                // use ImageIO to read the image in
                sourceImage = ImageIO.read(url);
            } catch (IOException e) {
                fail("Failed to load: " + ref);
            }

            // create an accelerated image of the right size to store our sprite in
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

            // draw our source image into the accelerated image
            image.getGraphics().drawImage(sourceImage, 0, 0, null);


            for (int k = 0; k < image.getWidth(); k++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    if (image.getRGB(k, j) == -16737793) {
                        image.setRGB(k, j, colour);
                    }
                }
            }
            // create a sprite, add it the cache then return it
            Sprite sprite = new Sprite(image);
            sprites.put(ref + colour, sprite);

            return sprite;
        }

    // otherwise, go away and grab the sprite from the resource
    // loader

    }

    /**
     * Utility method to handle resource loading failure
     * 
     * @param message The message to display on failure
     */
    private void fail(String message) {
        // we're pretty dramatic here, if a resource isn't available
        // we dump the message and exit the game
        System.err.println(message);
        System.exit(0);
    }
}