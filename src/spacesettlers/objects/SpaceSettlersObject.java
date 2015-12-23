package spacesettlers.objects;

import java.util.UUID;

import spacesettlers.graphics.SpacewarGraphics;
import spacesettlers.utilities.Position;

/**
 * Superclass for all objects in the spacesettlers simulator.  
 * 
 * @author amy
 */
abstract public class SpaceSettlersObject {
	/**
	 * Position of the object in the simulator space 
	 */
	Position position;
	
	/**
	 * The radius of the object
	 */
	int radius;
	
	/**
	 * The mass of the object
	 */
	protected int mass;
	
	/**
	 * Is the object alive?  Some objects can never die but some can.
	 */
	boolean isAlive;
	
	/**
	 * Is the object currently drawable on the GUI?
	 */
	boolean isDrawable;
	
	/**
	 * Is the object moveable?  Some objects can be flagged as not moveable and then
	 * they will not move even if things bounce into them.
	 */
	boolean isMoveable;
	
	/**
	 * The graphics for this object in the GUI
	 */
	SpacewarGraphics graphic;
	
	/**
	 * Is the object controlled through actions?  Ships and teams are.
	 */
	boolean isControllable;

	/**
	 * Id to track over cloning
	 */
	UUID id;
	
	/**
	 * All objects start at rest
	 */
	public SpaceSettlersObject(int mass, int radius) {
		this.mass = mass;
		this.radius = radius;
		position = new Position(0,0);
		this.id = UUID.randomUUID();

	}

	/**
	 * All objects start at rest
	 */
	public SpaceSettlersObject(int mass, int radius, Position position) {
		this.mass = mass;
		this.radius = radius;
		this.position = position;
		this.id = UUID.randomUUID();
	}

	
	/**
	 * @param isAlive set to true if the object is alive
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	/**
	 * @param isDrawable set to true if the object is drawable. Note it is only 
	 * drawn if it is alive as well.
	 */
	public void setDrawable(boolean isDrawable) {
		this.isDrawable = isDrawable;
	}
	
	/**
	 * Is this object alive?
	 * 
	 * @return the isAlive
	 */
	public boolean isAlive() {
		return isAlive;
	}

	/**
	 * Is this object drawable?
	 * 
	 * @return the isDrawable
	 */
	public boolean isDrawable() {
		return isDrawable;
	}

	/**
	 * @return the location
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Move the object (should only be called inside the simulator)
	 * 
	 * @param location
	 */
	public void setPosition(Position location) {
		this.position = location;
	}

	/**
	 * Return the radius of the object
	 * @return
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * Get the mass of the object
	 * @return
	 */
	public int getMass() {
		return mass;
	}


	/**
	 * @return the graphic, which is what is drawn in the graphics window
	 */
	public SpacewarGraphics getGraphic() {
		return graphic;
	}

	/**
	 * Is this object controlled by an external client?
	 * @return
	 */
	public boolean isControllable() {
		return isControllable;
	}

	/**
	 * is the object moveable? 
	 * @return
	 */
	public boolean isMoveable() {
		return isMoveable;
	}
	
	
	/**
	 * Return true if this object can respawn now and false otherwise.
	 * Assumes you can respawn immediately unless you override.
	 */
	public boolean canRespawn() {
		return true;
	}
	
	/**
	 * Gets the unique id for this object
	 * @return
	 */
	public UUID getId() {
		return id;
	}

	
	/**
	 * Hash on the UUID
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Equals on the UUID
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpaceSettlersObject other = (SpaceSettlersObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Make a deep copy of this object (deeply copying all pointers to avoid
	 * exploitation within the simulator)
	 * 
	 * @return
	 */
	abstract public SpaceSettlersObject deepClone();

	@Override
	public String toString() {
		return "SpaceSettlersObject at " + position;
	}
	
	/**
	 * Resets the UUID of the object (for respawning)
	 */
	public void resetId() {
		id = UUID.randomUUID();
	}
}
