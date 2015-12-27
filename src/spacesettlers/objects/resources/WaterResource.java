package spacesettlers.objects.resources;

import spacesettlers.objects.AbstractObject;

/**
 * A WaterResource for mining 
 * 
 * @author amy
 */
public class WaterResource extends AbstractResource {
	private static double DENSITY = 0.25;

	/**
	 * Make a new water resource
	 * @param radius
	 */
	public WaterResource(int radius) {
		super(radius, ResourceTypes.WATER);
	}

	/**
	 * Make a copy of this resource
	 */
	@Override
	public AbstractObject deepClone() {
		WaterResource newResource = new WaterResource(radius);
		newResource.id = id;
		return newResource;
	}

	@Override
	public double getDensity() {
		return DENSITY;
	}

}
