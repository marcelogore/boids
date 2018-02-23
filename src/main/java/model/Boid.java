package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Boid {
	
	public final static Color BACKGROUND_COLOR = Color.WHITE;
	
    private static int boidNr = 5;
    private static int velocityLimit = 10;
    private static int radiusLimit = 10;
//    private static int updateSkip = 1;
    private static int flockRadius = 10;

    private final int panelWidth;
    private final int panelHeight;
    private Vector position;
    private Vector positionToDraw;
    private Vector velocity = new Vector(0, 0);
    private Color color;
    private final static List<Boid> boids = new LinkedList<Boid>();
//    private static int updates = 0;
    private static int boidCounter = 0;
    private int boidNumber;

    public Boid(int pwidth, int pheight) {
        this.panelWidth = pwidth;
        this.panelHeight = pheight;
        setRandomPosition();
        setRandomColor();
        this.boidNumber = boidCounter++;
        this.positionToDraw = new Vector();
        System.out.println("Created boid " + this.boidNumber);
    }

    public Boid(Boid boid) {
        this.panelWidth = boid.panelWidth;
        this.panelHeight = boid.panelHeight;
        this.position = new Vector(boid.position);
        this.positionToDraw = new Vector(boid.positionToDraw);
        this.velocity = new Vector(boid.velocity);
        this.color = boid.color;
    }

    public static void initBoids(int pwidth, int pheight) {
        for (int i = 0; i < boidNr; i++) {
            boids.add(new Boid(pwidth, pheight));
        }
    }

    public static void drawBoids(Graphics g) {
        for (Boid boid : boids) {
        	System.out.println("Drawing boid " + boid.boidNumber);
            boid.draw(g);
        }
    }

    public static void updateBoids() {
//        updates++;
//        if (updates == updateSkip) {
//            updates = 1;
            for (Boid boid : boids) {
                boid.updateWithBounds();
            }
//        }
//        for (Boid boid : boids) {
//            // makes the boids move slowlier
//            boid.positionToDraw = new Vector(boid.position);
//            boid.positionToDraw.subtract(boid.velocity);
//            Vector ivel = new Vector(boid.velocity);
//            ivel.multiply(updates / (double) updateSkip);
//            boid.positionToDraw.add(ivel);
//        }
    }

    private void setRandomPosition() {
        Random rand = new Random();
        position = new Vector();
        position.x = rand.nextInt(panelWidth);
        position.y = rand.nextInt(panelHeight);
    }

    private void draw(Graphics g) {
    	
    	System.out.println("Position: " + (int) position.x + "," + (int) position.y);
    	System.out.println("Position to draw: " + (int) positionToDraw.x + "," + (int) positionToDraw.y);

    	// Erase current position
    	g.setColor(BACKGROUND_COLOR);
    	g.fillOval((int) position.x, (int) position.y, 5, 5);
    	
    	// Draw new position
    	g.setColor(color);
        g.fillOval((int) positionToDraw.x, (int) positionToDraw.y, 5, 5);
        
        position = new Vector(positionToDraw);
    }

    private void setRandomColor() {
        Random rand = new Random();
        this.color = new Color(rand.nextInt(256), rand.nextInt(256),
                rand.nextInt(256));
    }

    public void updateWithoutBounds() {
        velocity.add(rule1(), rule2(), rule3());
        limitVelocity();
        positionToDraw.add(velocity);

        if (positionToDraw.x > panelWidth) {
        	positionToDraw.x = 0;
        }
        if (positionToDraw.x < 0) {
        	positionToDraw.x = panelWidth;
        }
        if (positionToDraw.y > panelHeight) {
        	positionToDraw.y = 0;
        }
        if (positionToDraw.y < 0) {
        	positionToDraw.y = panelHeight;
        }
    }

    private void updateWithBounds() {
        
    	velocity.add(rule1(), rule2(), rule3());
        limitVelocity();
        positionToDraw.add(velocity);

        if (positionToDraw.x > panelWidth) {
        	positionToDraw.x = panelWidth;
            velocity.x = -velocity.x;
        }
        if (positionToDraw.x < 0) {
        	positionToDraw.x = 0;
            velocity.x = -velocity.x;
        }
        if (positionToDraw.y > panelHeight) {
        	positionToDraw.y = panelHeight;
            velocity.y = -velocity.y;
        }
        if (positionToDraw.y < 0) {
        	positionToDraw.y = 0;
            velocity.y = -velocity.y;
        }
    }

    /**
     * boids try to fly towards the centre of mass of neighbouring boids
     * 
     * @return resulting vector
     */
    private Vector rule1() {
    	
        Vector center = new Vector(0, 0);
        List<Boid> flock = flock();
        
        for (Boid boid : flock) {
            
        	if (!boid.equals(this)) {
                
        		center.add(boid.position);
            }
        }
        
        center.divide(flock.size() - 1);
        Vector result = new Vector(center);
        result.subtract(this.position);
        result.divide(100);
        
        return result;
    }

    /**
     * try to keep a small distance away from other objects
     * 
     * @return
     */
    private Vector rule2() {
        
    	Vector result = new Vector(0, 0);
        
    	for (Boid boid : flock()) {
            
    		if (!boid.equals(this)) {
                
    			if (isClose(boid.position)) {
                    
    				Vector sub = new Vector(boid.position);
                    sub.subtract(position);
                    result.subtract(sub);
                }
            }
        }
    	
        return result;
    }

    /**
     * boids try to match velocity with near boids
     * 
     * @return
     */
    private Vector rule3() {
        Vector pvj = new Vector(0, 0);
        List<Boid> flock = flock();
        for (Boid boid : flock) {
            if (!boid.equals(this)) {
                pvj.add(boid.velocity);
            }
        }
        pvj.divide(flock.size() - 1);
        pvj.subtract(velocity);
        pvj.divide(8);
        return pvj;
    }

    private void limitVelocity() {
        if (velocity.length() > velocityLimit) {
            velocity.divide(velocity.length());
            velocity.multiply(velocityLimit);
        }
    }

    public List<Boid> flock() {
        List<Boid> flock = new LinkedList<Boid>();
        for (Boid boid : boids) {
            if (isInFlock(boid.position)) {
                flock.add(boid);
            }
        }
        return flock;
    }

    private boolean isInFlock(final Vector pos) {
        Vector range = new Vector(position);
        range.subtract(pos);
        return range.length() < flockRadius;
    }

    private boolean isClose(final Vector pos) {
        Vector range = new Vector(position);
        range.subtract(pos);
        return range.length() < radiusLimit;
    }

}