package spacesettlers.objects.resources;

import spacesettlers.objects.AbstractObject;

/**
 * A metal based resource.  Metal is the heaviest of the resources.
 * 
 * @author amy
 */
public class MetalsResource extends AbstractResource {
	public static double DENSITY = 0.45;

	/**
	 * Make a new metals resource
	 * @param radius
	 */
	public MetalsResource(int radius) {
		super(radius, ResourceTypes.METALS);
	}

	@Override
	public double getDensity() {
		return DENSITY;
	}

	@Override
	public AbstractObject deepClone() {
		MetalsResource newMetal = new MetalsResource(radius);
		newMetal.id = id;
		return newMetal;
	}

}
