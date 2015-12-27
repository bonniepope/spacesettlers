package spacesettlers.objects.resources;

import spacesettlers.objects.AbstractObject;

/**
 * A fuel resource (plutonium?) for mining
 * 
 * @author amy
 */
public class FuelResource extends AbstractResource {
	private static double DENSITY = 0.35;

	/**
	 * Make a new Fuel resource
	 * 
	 * @param radius
	 */
	public FuelResource(int radius) {
		super(radius, ResourceTypes.FUEL);
	}
	
	/**
	 * Make a copy for security
	 */
	@Override
	public AbstractObject deepClone() {
		FuelResource newFuel = new FuelResource(radius);
		newFuel.id = id;
		return newFuel;
	}
	
	@Override
	public double getDensity() {
		return DENSITY;
	}
}
