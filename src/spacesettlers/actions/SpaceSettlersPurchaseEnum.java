package spacesettlers.actions;

import spacesettlers.powerups.SpaceSettlersPowerupEnum;

/**
 * An enum of the available purchases and their initial costs (they may change as a team buys more)
 * @author amy
 *
 */
public enum SpaceSettlersPurchaseEnum {
	BASE(1000),
	SHIP(3000),
	POWERUP_SHIELD(2000,SpaceSettlersPowerupEnum.TOGGLE_SHIELD),
	POWERUP_EMP_LAUNCHER(1000, SpaceSettlersPowerupEnum.FIRE_EMP),
	POWERUP_MINE_LAUNCHER(Integer.MAX_VALUE, SpaceSettlersPowerupEnum.LAY_MINE),
	POWERUP_BASE_TURRET(Integer.MAX_VALUE, SpaceSettlersPowerupEnum.FIRE_TURRET),
	POWERUP_HEAT_SEEKING_MISSILE_LAUNCHER(Integer.MAX_VALUE, SpaceSettlersPowerupEnum.FIRE_HEAT_SEEKING_MISSILE),
	POWERUP_DOUBLE_BASE_HEALING_SPEED(3000, SpaceSettlersPowerupEnum.DOUBLE_BASE_HEALING_SPEED),
	POWERUP_DOUBLE_MAX_ENERGY(2000, SpaceSettlersPowerupEnum.DOUBLE_MAX_ENERGY),
	POWERUP_DOUBLE_WEAPON_CAPACITY(3000, SpaceSettlersPowerupEnum.DOUBLE_WEAPON_CAPACITY),
	NOTHING(0);
	
	/**
	 * Initial cost to buy the item
	 */
	int initialCost;
	
	/**
	 * Map to the power up (if it exists)
	 */
	SpaceSettlersPowerupEnum powerupMap;
	
	SpaceSettlersPurchaseEnum(int initialCost) {
		this.initialCost = initialCost;
		this.powerupMap = null;
	}

	SpaceSettlersPurchaseEnum(int initialCost, SpaceSettlersPowerupEnum powerupMap) {
		this.initialCost = initialCost;
		this.powerupMap = powerupMap;
	}

	/**
	 * Get the initial cost of buying this item
	 * @return
	 */
	public int getInitialCost() {
		return initialCost;
	}

	public SpaceSettlersPowerupEnum getPowerupMap() {
		return powerupMap;
	}
	
	

}
