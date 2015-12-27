package spacesettlers.objects.resources;

import java.util.UUID;

import spacesettlers.objects.AbstractObject;
import spacesettlers.utilities.Position;

/**
 * A resource for Space Settlers.  Resources can be mined from asteroids and picked up by ships and deposited at bases.
 *  
 * @author amy
 *
 */
abstract public class AbstractResource extends AbstractObject {
	/**
	 * The type of resource this is
	 */
	ResourceTypes type;

	/**
	 * The quantity of resource available (for now, the mass of the resource but that can be adjusted later)
	 */
	protected int resourceQuantity;

	/**
	 * Create a resource and compute its mass
	 * @param radius
	 */
	public AbstractResource(int radius, ResourceTypes type) {
		super(0, radius);
		super.setMass(computeMass());
		resourceQuantity = mass;
		this.type = type;
	}

	abstract public double getDensity();

	/**
	 * Compute the mass for this resource (each resource has a different density and its mass is based on 
	 * its density and surface area)
	 * @return
	 */
	public int computeMass() {
		return (int) Math.round(Math.PI * radius * radius * getDensity());
	}

	/**
	 * Get the type of resource
	 * @return
	 */
	public ResourceTypes getType() {
		return type;
	}

	/**
	 * Get the quantity of this resource
	 * @return
	 */
	public int getResourceQuantity() {
		return resourceQuantity;
	}


}
