package spacesettlers.objects;

import spacesettlers.graphics.AsteroidGraphics;
import spacesettlers.objects.resources.AbstractResource;
import spacesettlers.objects.resources.FuelResource;
import spacesettlers.objects.resources.MetalsResource;
import spacesettlers.objects.resources.ResourceTypes;
import spacesettlers.objects.resources.WaterResource;
import spacesettlers.utilities.Position;

/**
 * An asteroid that can or cannot be mined
 * 
 * @author amy
 *
 */
public class Asteroid extends AbstractObject {
	public static final int MIN_ASTEROID_RADIUS = 5;
	public static final int MAX_ASTEROID_RADIUS = 15;
	public static final int ASTEROID_MASS = 1000;
	
	/**
	 * If an asteroid is mineable, this will be non-null and proportional to its radius
	 */
	AbstractResource resource;
	
    /**
     * Is the asteroid mineable?
     */
    boolean isMineable;
    
    /**
     * What kind of resource does the asteroid provide (if it is mineable)?
     */
    ResourceTypes type;
	
    public Asteroid(Position location, boolean mineable, int radius, boolean moveable, ResourceTypes type) {
		super(ASTEROID_MASS, radius, location);
		
		setDrawable(true);
		setAlive(true);
		isMineable = mineable;
		graphic = new AsteroidGraphics(this);
		this.isMoveable = moveable;
		this.type = type;
		
		if (isMineable) {
			resetResources();
		} else {
			resource = null;
		}
		
	}
    
    /**
     * Make a copy for security
     */
    public Asteroid deepClone() {
    	Asteroid newAsteroid = new Asteroid(getPosition().deepCopy(), isMineable, radius, isMoveable, type);
    	newAsteroid.setAlive(isAlive);
    	newAsteroid.id = id;
    	return newAsteroid;
    }
    
    
    /**
     * Sets the resource value based on the radius
     */
    private void resetResources() {
    	switch (type) {
    		case WATER:
    			resource = new WaterResource(radius);
    			break;
    		
    		case FUEL:
    			resource = new FuelResource(radius);
    			break;
    			
    		case METALS:
    			resource = new MetalsResource(radius);
    			break;
    	}
    }
    
    /**
     * Return the resource for this asteroid
     * @return
     */
	public AbstractResource getResource() {
		return resource;
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
	public ResourceTypes getType() {
		return type;
	}

	
	
}
