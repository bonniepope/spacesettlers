package spacesettlers.objects;

import spacesettlers.graphics.AsteroidGraphics;
import spacesettlers.objects.resources.ResourceFactory;
import spacesettlers.objects.resources.ResourcePile;
import spacesettlers.objects.resources.ResourceTypes;
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
	
	private double fuelProportion, waterProportion, metalsProportion;
	
    /**
     * Is the asteroid mineable?
     */
    boolean isMineable;
    
    public Asteroid(Position location, boolean mineable, int radius, boolean moveable, double fuel, double water, double metals) {
		super(ASTEROID_MASS, radius, location);
		
		setDrawable(true);
		setAlive(true);
		isMineable = mineable;
		graphic = new AsteroidGraphics(this);
		this.isMoveable = moveable;
		this.fuelProportion = fuel;
		this.waterProportion = water;
		this.metalsProportion = metals;
		
		if (isMineable) {
			resetResources();
		} 
		
	}
    
    /**
     * Make a copy for security
     */
    public Asteroid deepClone() {
    	Asteroid newAsteroid = new Asteroid(getPosition().deepCopy(), isMineable, radius, isMoveable, 
    			fuelProportion, waterProportion, metalsProportion);
    	newAsteroid.setAlive(isAlive);
    	newAsteroid.id = id;
    	return newAsteroid;
    }
    
    
    /**
     * Sets the resource value based on the radius
     */
    public void resetResources() {
    	resources.setResources(ResourceTypes.FUEL, ResourceFactory.getResourceQuantity(ResourceTypes.FUEL, radius));
    	resources.setResources(ResourceTypes.WATER, ResourceFactory.getResourceQuantity(ResourceTypes.WATER, radius));
    	resources.setResources(ResourceTypes.METALS, ResourceFactory.getResourceQuantity(ResourceTypes.METALS, radius));
    }
    
	/**
	 * @return the isMineable
	 */
	public boolean isMineable() {
		return isMineable;
	}

	/**
	 * Get the fuel proportion (used for graphics but maybe useful in other ways)
	 * @return the proportion of the asteroid dedicated to fuel
	 */
	public double getFuelProportion() {
		return fuelProportion;
	}

	/**
	 * Get the water proportion (used for graphics but maybe useful in other ways)
	 * @return the proportion of the asteroid dedicated to water
	 */
	public double getWaterProportion() {
		return waterProportion;
	}

	/**
	 * Get the metals proportion (used for graphics but maybe useful in other ways)
	 * @return the proportion of the asteroid dedicated to metals
	 */
	public double getMetalsProportion() {
		return metalsProportion;
	}

	public String toString() {
		String str = "Asteroid id " + super.id + " mass " + mass + " resources " + resources;
		return str;
		
	}
	
}
