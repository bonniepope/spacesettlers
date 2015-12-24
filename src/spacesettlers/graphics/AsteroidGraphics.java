package spacesettlers.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import spacesettlers.gui.JSpaceSettlersComponent;
import spacesettlers.objects.Asteroid;
import spacesettlers.objects.SpaceSettlersResourcesEnum;
import spacesettlers.utilities.Position;

/**
 * Asteroid graphics - mostly taken from the obstacle shadow in spacewar1
 * 
 * @author amy
 */
public class AsteroidGraphics extends SpacewarGraphics {
    public static final Color REGULAR_ASTEROID_COLOR = new Color(126, 96, 58);
    public static final Color REGULAR_LINE_COLOR = new Color(162, 124, 76);
    public static final Color WATER_ASTEROID_COLOR = new Color(0,191,255);
    public static final Color WATER_LINE_COLOR = new Color(30,144,255);
    public static final Color FUEL_ASTEROID_COLOR = new Color(0,255,0);
    public static final Color FUEL_LINE_COLOR = new Color(50,205,50);
    public static final Color METALS_ASTEROID_COLOR = new Color(192, 192, 192);
    public static final Color METALS_LINE_COLOR = new Color(211, 211, 211);
    public static final Color MOVEABLE_LINE_COLOR = new Color(1, 124, 76);
    public static final Color MONEY_COLOR = Color.WHITE;

    Asteroid asteroid;
    
    public AsteroidGraphics(Asteroid asteroid) {
		super(asteroid.getRadius(), asteroid.getRadius());
		this.asteroid = asteroid;
	}


	@Override
	public void draw(Graphics2D graphics) {
        final float radius = asteroid.getRadius();
        final float diameter = asteroid.getRadius() * 2;

        final Ellipse2D.Double shape = new Ellipse2D.Double(drawLocation.getX() - radius,
        		drawLocation.getY() - radius, diameter, diameter);

        // show minable asteroids in a different color
        if (asteroid.isMineable()) {
        	switch (asteroid.getAsteroidType()) {
        	case FUEL:
        		graphics.setColor(FUEL_ASTEROID_COLOR);
        		break;
        	case WATER:
        		graphics.setColor(WATER_ASTEROID_COLOR);
        		break;
        	case METALS:
        		graphics.setColor(METALS_ASTEROID_COLOR);
        	}
        } else {
        	graphics.setColor(REGULAR_ASTEROID_COLOR);
        }
        graphics.fill(shape);

        /*
        // if the asteroid is moveable, give it a different color outline
        graphics.setStroke(JSpaceSettlersComponent.STROKE);
        if (asteroid.isMoveable()) {
        	graphics.setColor(MOVEABLE_LINE_COLOR);
        } else if (asteroid.isMineable()) {
        	switch (asteroid.getAsteroidType()) {
        	case FUEL:
        		graphics.setColor(FUEL_LINE_COLOR);
        		break;
        	case WATER:
        		graphics.setColor(WATER_LINE_COLOR);
        		break;
        	case METALS:
        		graphics.setColor(METALS_LINE_COLOR);
        	}
        } else {
        	graphics.setColor(REGULAR_LINE_COLOR);
        }
        */
        graphics.draw(shape);

        
        // put the resourcesAvailable an asteroid can be mined for in the center of the asteroid
        if (asteroid.isMineable()) {
            //graphics.setFont(JSpaceSettlersComponent.FONT8);
        	//graphics.setColor(MONEY_COLOR);
        	//graphics.drawString(Integer.toString(asteroid.getResourcesAvailable()), 
        	//		(int)drawLocation.getX() - radius + 2, (int)drawLocation.getY() - 6);
        }
        
	}

	/**
	 * Only draw the asteroid if it alive and drawable
	 */
	public boolean isDrawable() {
		return (asteroid.isAlive() && asteroid.isDrawable());
	}

	/**
	 * Return the actual location of the asteroid
	 */
	public Position getActualLocation() {
		return asteroid.getPosition();
	}

}
