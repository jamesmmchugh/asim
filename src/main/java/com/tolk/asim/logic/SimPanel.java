        /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tolk.asim.logic;

        import java.awt.*;
        import java.awt.event.MouseEvent;
        import java.awt.event.MouseWheelEvent;
        import java.awt.event.MouseWheelListener;
        import java.awt.image.BufferStrategy;
        import java.awt.image.BufferedImage;

        import javax.swing.*;
        import javax.swing.event.MouseInputListener;

        import com.tolk.asim.simulation.Simulation;

        /**
         *
         * @author jmmchugh
         */
        public class SimPanel extends JPanel implements MouseWheelListener, MouseInputListener {
            //Simulation

            Simulation sim;
            //Viewer
            JViewport viewport;
            Dimension viewSize;
            BufferStrategy strat;
            //Drawing
            Image renderImage;
            Graphics2D g2d;
            boolean draw = true;
            double scale = 1.0;
            Object drawLock = new Object();
            //Mouse events
            int m_XDifference, m_YDifference;
            Dimension defaultSize;

            public SimPanel(JViewport viewport) {
                this.viewport = viewport;
                this.addMouseListener(this);
                this.addMouseMotionListener(this);
                this.addMouseWheelListener(this);

                //this.setup();
            }

            public SimPanel(Simulation sim, JViewport viewport) {
                this.sim = sim;
                defaultSize = new Dimension(sim.getWorld().getSize(), sim.getWorld().getSize());
                this.setPreferredSize(defaultSize);
                this.viewport = viewport;
                this.addMouseListener(this);
                this.addMouseMotionListener(this);
                this.addMouseWheelListener(this);
                //this.setup();
            }

            //Used to initialise the buffered image once drawing begins
            private void setup() {
                synchronized (drawLock) {
                    viewSize = viewport.getExtentSize();
                    renderImage = new BufferedImage(viewSize.width, viewSize.height, BufferedImage.TYPE_INT_RGB);
                    g2d = (Graphics2D) renderImage.getGraphics();
                }
            }

            @Override
            public void paintComponent(Graphics g) {
                paintSimulation(g);
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }

            //Paint the screen for a specific simulation
            public void paintSimulation(Simulation sim, Graphics g) {
                synchronized (drawLock) {
                    setSimulation(sim);
                    paintSimulation(g);
                }
            }

            //Paint the screen with the panels simulation
            public void paintSimulation(Graphics g) {
                //if no image, then init
                if (renderImage == null) {
                    setup();
                }
                //clear the screen
                resetScreen();
                //draw the simulation if not null, to the image
                if (sim != null) {
                    sim.draw(this);
                }
                //paint the screen with the image
                paintScreen(g);
            }

            private void resetScreen() {
                Dimension newSize = viewport.getExtentSize();
                if (viewSize.height != newSize.height || viewSize.width != newSize.width || renderImage == null) {
                    //System.out.println("Screen Size Changed: " + viewSize + "   " + newSize);
                    viewSize = newSize;
                    renderImage = new BufferedImage(viewSize.width, viewSize.height, BufferedImage.TYPE_INT_RGB);
                    g2d = (Graphics2D) renderImage.getGraphics();
                } else {
                    g2d.setBackground(Color.DARK_GRAY);
                    g2d.clearRect(0, 0, (int) (viewSize.width), (int) (viewSize.height));
                }
            }

            private void paintScreen(Graphics g) {
                Graphics2D g2;
                try {
                    //g = viewport.getGraphics();
                    //g = this.getGraphics();
                    g2 = (Graphics2D) g;
                    if ((g != null) && (renderImage != null)) {
                        g2.drawImage(renderImage, (int) viewport.getViewPosition().getX(), (int) viewport.getViewPosition().getY(), null);
                    }
                    Toolkit.getDefaultToolkit().sync();  // sync the display on some systems
                    g.dispose();
                    g2.dispose();
                    this.revalidate();
                } catch (Exception e) {
                    System.out.println("Graphics context error: " + e);
                }
            }

            //Simulation makes calls to this method to draw items on the image
            public void draw(BufferedImage image, int x, int y, Color colour) {
                Rectangle r = viewport.getViewRect();
                if (g2d != null && draw) {
                    Point p = new Point((int) (x * scale), (int) (y * scale));
                    if (r.contains(p)) {
                        if (scale < 1) {
                            Graphics2D g2 = (Graphics2D) image.getGraphics();
                            Image test = image.getScaledInstance((int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), Image.SCALE_FAST);
                            g2d.drawImage(test, (int) ((x * scale - r.x)), (int) ((y * scale - r.y)), null);
                        } else {
                            g2d.drawImage(image, x - r.x, y - r.y, null);
                        }
                    }
                }
            }

            public void setDraw(boolean draw) {
                this.draw = draw;
            }

            public void setSimulation(Simulation sim) {
                if (!(this.sim == sim)) {
                    this.sim = sim;
                    this.scale = 1;
                    this.setPreferredSize(new Dimension(sim.getWorld().getSize(), sim.getWorld().getSize()));
                }
            }

            public void setDefaultSize(Dimension defaultSize) {
                this.defaultSize = defaultSize;
            }


            public void mouseWheelMoved(MouseWheelEvent e) {
                synchronized (drawLock) {
                    updatePreferredSize(e.getWheelRotation(), e.getPoint());
                }
            }

            private void updatePreferredSize(int wheelRotation, Point stablePoint) {
                double scaleFactor = findScaleFactor(wheelRotation);
                if (scale * scaleFactor < 1 && scale * scaleFactor > 0.05) {
                    scaleBy(scaleFactor);
                    Point offset = findOffset(stablePoint, scaleFactor);
                    offsetBy(offset);
                    this.getParent().doLayout();
                }
            }

            private double findScaleFactor(int wheelRotation) {
                double d = wheelRotation * 1.08;
                return (d > 0) ? 1 / d : -d;
            }

            private void scaleBy(double scaleFactor) {
                int w = (int) (this.getWidth() * scaleFactor);
                int h = (int) (this.getHeight() * scaleFactor);
                this.setPreferredSize(new Dimension(w, h));
                this.scale = this.scale * scaleFactor;
            }

            private Point findOffset(Point stablePoint, double scaleFactor) {
                int x = (int) (stablePoint.x * scaleFactor) - stablePoint.x;
                int y = (int) (stablePoint.y * scaleFactor) - stablePoint.y;
                return new Point(x, y);
            }

            private void offsetBy(Point offset) {
                Point location = viewport.getViewPosition();
                //this.setLocation(location.x - offset.x, location.y - offset.y);
                viewport.setViewPosition(new Point(location.x + offset.x, location.y + offset.y));
            }

            public void mouseDragged(MouseEvent e) {
                //Point p = this.getLocation();
                Point p = viewport.getViewPosition();
                int newX = p.x - (e.getX() - m_XDifference);
                int newY = p.y - (e.getY() - m_YDifference);
                //this.setLocation(newX, newY);
                newX = newX > 0 ? newX : 0;
                newY = newY > 0 ? newY : 0;
                viewport.setViewPosition(new Point(newX, newY));
                //this.getParent().doLayout();
            }

            public void mousePressed(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                m_XDifference = e.getX();
                m_YDifference = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseClicked(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseMoved(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void reScale(double zoom) {
                this.scale = zoom;
            }
        }
