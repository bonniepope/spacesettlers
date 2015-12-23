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
	public static final double MONEY_DENSITY = 0.5;
	
	/**
	 * If an asteroid is mineable, this will be non-zero and proportional to its radius
	 */
	int money;
	
    /**
     * Is the asteroid mineable?
     */
    boolean isMineable;
	
    public Asteroid(Position location, boolean mineable, int radius, boolean moveable) {
		super(ASTEROID_MASS, radius, location);
		
		setDrawable(true);
		setAlive(true);
		isMineable = mineable;
		graphic = new AsteroidGraphics(this);
		this.isMoveable = moveable;
		
		if (isMineable) {
			resetMoney();
		}
	}
    
    /**
     * Make a copy for security
     */
    public Asteroid deepClone() {
    	Asteroid newAsteroid = new Asteroid(getPosition().deepCopy(), isMineable, radius, isMoveable);
    	newAsteroid.setAlive(isAlive);
    	newAsteroid.id = id;
    	return newAsteroid;
    }
    
    
    /**
     * Sets the money value based on the radius
     */
    private void resetMoney() {
    	money = (int) (radius * radius * Math.PI * MONEY_DENSITY);
    }
    
    /**
     * Return how much money this asteroid is worth
     * @return
     */
    public int getMoney() {
    	return money;
    }
    

	/**
	 * @return the isMineable
	 */
	public boolean isMineable() {
		return isMineable;
	}

}
