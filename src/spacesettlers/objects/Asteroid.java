package spacesettlers.objects;

import spacesettlers.graphics.AsteroidGraphics;
import spacesettlers.utilities.Position;

/**
 * An asteroid that can or cannot be mined
 * 
 * @author amy
 *
 */
public class Asteroid extends SpaceSettlersObject {
	public static final int MIN_ASTEROID_RADIUS = 5;
	public static final int MAX_ASTEROID_RADIUS = 15;
	public static final int ASTEROID_MASS = 1000;
	public static final double RESOURCE_DENSITY = 0.5;
	
	/**
	 * If an asteroid is mineable, this will be non-zero and proportional to its radius
	 */
	int resourcesAvailable;
	
    /**
     * Is the asteroid mineable?
     */
    boolean isMineable;
    
    /**
     * What kind of resource does the asteroid provide (if it is mineable)?
     */
    SpaceSettlersResourcesEnum asteroidType;
	
    public Asteroid(Position location, boolean mineable, int radius, boolean moveable, SpaceSettlersResourcesEnum type) {
		super(ASTEROID_MASS, radius, location);
		
		setDrawable(true);
		setAlive(true);
		isMineable = mineable;
		graphic = new AsteroidGraphics(this);
		this.isMoveable = moveable;
		asteroidType = type;
		
		if (isMineable) {
			resetResources();
		}
		
	}
    
    /**
     * Make a copy for security
     */
    public Asteroid deepClone() {
    	Asteroid newAsteroid = new Asteroid(getPosition().deepCopy(), isMineable, radius, isMoveable, asteroidType);
    	newAsteroid.setAlive(isAlive);
    	newAsteroid.id = id;
    	return newAsteroid;
    }
    
    
    /**
     * Sets the resource value based on the radius
     */
    private void resetResources() {
    	resourcesAvailable = (int) (radius * radius * Math.PI * RESOURCE_DENSITY);
    }
    
    /**
     * Return how much of the resource this asteroid is worth
     * @return
     */
    public int getResourcesAvailable() {
    	return resourcesAvailable;
    }
    

	/**
	 * @return the isMineable
	 */
	public boolean isMineable() {
		return isMineable;
	}

	/**
	 * Return the type of the asteroid
	 * @return
	 */
	public SpaceSettlersResourcesEnum getAsteroidType() {
		return asteroidType;
	}

	
	
}
