package spacesettlers.clients;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import spacesettlers.actions.SpaceSettlersAction;
import spacesettlers.actions.SpaceSettlersPurchaseEnum;
import spacesettlers.graphics.SpacewarGraphics;
import spacesettlers.objects.Base;
import spacesettlers.objects.Ship;
import spacesettlers.objects.SpaceSettlersActionableObject;
import spacesettlers.powerups.SpaceSettlersPowerupEnum;
import spacesettlers.simulator.SpaceSettlersSimulator;
import spacesettlers.simulator.Toroidal2DPhysics;

/**
 * A team holds the ships and a pointer to the client
 * that controls the ships.  They are separated for security inside
 * the client (to keep the client from directly manipulating ships)
 * 
 * @author amy
 */
public class Team {
	/**
	 * The set of ships owned by this team
	 */
	Set<Ship> teamShips;
	
	/**
	 * The set of bases associated with this team (bases are not
	 * stored directly because they point to team and then cloning
	 * causes a stack overflow)
	 */
	Set<UUID> teamBaseIDs;
	
	/**
	 * A set of all the ids associated with the team (used to verify
	 * power ups)
	 */
	Set<UUID> teamIDs;

	/**
	 * The team color (used by the GUI)
	 */
	Color teamColor;
	
	/**
	 * The name of the team
	 */
	String teamName;
	
	/**
	 * The client for this team
	 */
	TeamClient teamClient;
	
	/**
	 * current team score (set in the simulator, which knows how the team is being scored)
	 */
	double score;
	
	/**
	 * available (unspent) resourcesAvailable from the asteroids and the total resourcesAvailable earned
	 */
	int availableMoney, totalMoney;
	
	/**
	 * Keep track of the total beacons collected (for the ladder)
	 */
	int totalBeacons;
	
	/**
	 * The name that shows up in the ladder
	 */
	String ladderName;
	
	/**
	 * The current costs for this team to buy items (some change
	 * as you buy more and more of them)
	 */
	Map<SpaceSettlersPurchaseEnum, Integer> costToPurchase;
	
	/**
	 * The maximum number of ships for this team
	 */
	int maxNumberShips;
	
	/**
	 * Thread for this team
	 */
	ExecutorService executor;
	
	/**
	 * Initialize the team client to have an empty list of ships.
	 */
	public Team(TeamClient teamClient, String ladderName, int maxNumberShips) {
		this.teamShips = new HashSet<Ship>();
		this.teamBaseIDs = new HashSet<UUID>();
		this.teamIDs = new HashSet<UUID>();
		this.teamClient = teamClient;
		this.teamColor = teamClient.getTeamColor();
		this.teamName = teamClient.getTeamName();
		this.ladderName = ladderName;
		costToPurchase = new HashMap<SpaceSettlersPurchaseEnum, Integer>();
		resetCostToPurchase();
		this.maxNumberShips = maxNumberShips;
		
		executor = null;
	}
	
	/**
	 * Reset the costs to purchase new items
	 */
	private void resetCostToPurchase() {
		for (SpaceSettlersPurchaseEnum purchase : SpaceSettlersPurchaseEnum.values()) {
			costToPurchase.put(purchase, purchase.getInitialCost());
		}
	}

	/**
	 * Make a deep copy for security
	 * 
	 * @return
	 */
	public Team deepCopy() {
		Team newTeam = new Team(teamClient, ladderName, maxNumberShips);
		
		for (Ship ship : teamShips) {
			newTeam.addShip(ship.deepClone());
		}
		
		newTeam.teamBaseIDs.addAll(this.teamBaseIDs);
		newTeam.costToPurchase.putAll(costToPurchase);
		return newTeam;
	}
	
	/**
	 * Return the maximum number of ships for this team
	 * @return
	 */
	public int getMaxNumberShips() {
		return maxNumberShips;
	}

	/**
	 * Make a cloned list of the team ships
	 * 
	 * @return
	 */
	private Set<Ship> getTeamShipsClone() {
		Set<Ship> clonedShips = new HashSet<Ship>();
		
		for (Ship ship : teamShips) {
			clonedShips.add(ship.deepClone());
		}
		return clonedShips;
	}
	
	/**
	 * Make a cloned list of the team's actionable objects (ships and bases)
	 * 
	 * @return
	 */
	private Set<SpaceSettlersActionableObject> getTeamActionableObjectsClone(Toroidal2DPhysics space) {
		Set<SpaceSettlersActionableObject> clones = new HashSet<SpaceSettlersActionableObject>();
		
		for (Ship ship : teamShips) {
			clones.add(ship.deepClone());
		}
		
		for (UUID baseId : teamBaseIDs) {
			clones.add((Base)space.getObjectById(baseId).deepClone());
		}
		
		return clones;
	}

	
	
	/**
	 * Add a ship to the team
	 * @param ship
	 */
	public void addShip(Ship ship) {
		teamShips.add(ship);
		addTeamID(ship.getId());
	}
	
	/**
	 * Return the list of ships
	 * @return
	 */
	public Set<Ship> getShips() {
		return teamShips;
	}

	/**
	 * Add a base to the team's list of bases
	 * 
	 * @param base
	 */
	public void addBase(Base base) {
		teamBaseIDs.add(base.getId());
		addTeamID(base.getId());
	}
	
	/**
	 * Remove the base because it died
	 * @param base
	 */
	public void removeBase(Base base) {
		teamBaseIDs.remove(base.getId());
		removeTeamID(base.getId());
	}
	
	/**
	 * Add a team id to the list of ids for the team
	 * @param id
	 */
	private void addTeamID(UUID id) {
		teamIDs.add(id);
	}
	
	/**
	 * Remove an object from the list for the team
	 * @param id
	 */
	private void removeTeamID(UUID id) {
		teamIDs.remove(id);
	}
	
	/**
	 * Returns true if this UUID is associated with this team and false otherwise.
	 * Used to keep teams from calling power ups on other team's objects
	 * 
	 * @param id
	 * @return
	 */
	public boolean isValidTeamID(UUID id) {
		if (teamIDs.contains(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return the teamColor
	 */
	public Color getTeamColor() {
		return teamColor;
	}

	/**
	 * @param teamColor the teamColor to set
	 */
	public void setTeamColor(Color teamColor) {
		this.teamColor = teamColor;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * Get the current cost of the specified item
	 * @return
	 */
	public int getCurrentCost(SpaceSettlersPurchaseEnum item) {
		return costToPurchase.get(item);
	}

	/**
	 * Change the cost of an item (done after a purchase)
	 * 
	 * The new costs are embedded here rather than in the simulator to make
	 * it easy to find (since they are stored here)
	 */
	public void updateCost(SpaceSettlersPurchaseEnum item) {
		int currentCost = costToPurchase.get(item);
		switch (item) {
		case BASE:
			costToPurchase.put(item, currentCost * 2);
			break;

		case SHIP:
			costToPurchase.put(item, currentCost * 2);
			break;

		case POWERUP_SHIELD:
			// nothing
			break;

		case POWERUP_EMP_LAUNCHER:
			// nothing
			break;
			
		case POWERUP_MINE_LAUNCHER:
			// nothing
			break;
			
		case POWERUP_BASE_TURRET:
			// nothing
			break;

		case POWERUP_HEAT_SEEKING_MISSILE_LAUNCHER:
			// nothing
			break;
			
		case POWERUP_DOUBLE_BASE_HEALING_SPEED:
			costToPurchase.put(item, currentCost * 2);
			break;
			
		case POWERUP_DOUBLE_MAX_ENERGY:
			costToPurchase.put(item, currentCost * 2);
			break;
			
		case POWERUP_DOUBLE_WEAPON_CAPACITY:
			costToPurchase.put(item, currentCost * 2);
			break;
	
		case NOTHING:
			// nothing changes here
		}
		
	}

	/**
	 * Ask the team client for actions
	 * 
	 * @param spacewarSimulator
	 * @param random
	 * @return
	 */
	public Map<UUID, SpaceSettlersAction> getTeamMovementStart(Toroidal2DPhysics space) {
        Map<UUID, SpaceSettlersAction> teamActions = new HashMap<UUID, SpaceSettlersAction>();

		// ask the client for its movement
		final Toroidal2DPhysics clonedSpace = space.deepClone();
		final Set<SpaceSettlersActionableObject> clonedActionableObjects = getTeamActionableObjectsClone(space);
		
		// if the previous thread call hasn't finished, then just return default
		if (executor == null || executor.isTerminated()) {
			executor = Executors.newSingleThreadExecutor();
		} else {
			return teamActions;
		}
		
        Future<Map<UUID, SpaceSettlersAction>> future = executor.submit(
        		new Callable<Map<UUID, SpaceSettlersAction>>(){
        			public Map<UUID, SpaceSettlersAction> call() {
        				Map<UUID, SpaceSettlersAction> teamActions = null;
        				try {
        					teamActions = teamClient.getMovementStart(clonedSpace, clonedActionableObjects);
        				} catch (Exception e) {
        					// we shouldn't do this but it seems necessary to make
        					// the agent behave (do nothing) if it crashes
        		        	teamActions = new HashMap<UUID, SpaceSettlersAction>();
        				}
        				return teamActions;
        			}
        		});
        
        try {
            //start
            teamActions = future.get(SpaceSettlersSimulator.TEAM_ACTION_TIMEOUT, TimeUnit.MILLISECONDS);
            //finished in time
        } catch (TimeoutException e) {
            //was terminated
        	//return empty map, this will invoke default behavior of using DoNothingAction
        	teamActions = new HashMap<UUID, SpaceSettlersAction>();
        	System.out.println(getTeamName() + " timed out in getTeamMovementStart");
        } catch (InterruptedException e) {
        	//we were interrupted (should not happen but lets be good programmers) 
        	//return empty map, this will invoke default behavior of using DoNothingAction
        	teamActions = new HashMap<UUID, SpaceSettlersAction>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			//the executor threw and exception (should not happen but lets be good programmers) 
        	//return empty map, this will invoke default behavior of using DoNothingAction
        	teamActions = new HashMap<UUID, SpaceSettlersAction>();
			e.printStackTrace();
		} 

        executor.shutdownNow();
        
        return teamActions;
		
// 		HashMap<UUID, SpaceSettlersAction> teamActions = teamClient.getAction(clonedSpace, clonedTeamShips);
//		return teamActions;
	}

	/**
	 * Allows the client to do cleanup after an action and before
	 * the next one (if needed)
	 * 
	 * @param simulatedSpace
	 * @return
	 */
	public void getTeamMovementEnd(Toroidal2DPhysics space) {
		final Toroidal2DPhysics clonedSpace = space.deepClone();
		final Set<SpaceSettlersActionableObject> clonedActionableObjects = getTeamActionableObjectsClone(space);

		// if the previous thread call hasn't finished, then just return default
		if (executor == null || executor.isTerminated()) {
			executor = Executors.newSingleThreadExecutor();
		} else {
			return;
		}

		//System.out.println("exec " + executor.isTerminated());
        Future<Boolean> future = executor.submit(
        		new Callable<Boolean>(){
        			public Boolean call() throws Exception {
        				teamClient.getMovementEnd(clonedSpace, clonedActionableObjects);
        				return true;
        			}
        		});
        
        Boolean didReturn = false;
        try {
            //start
        	didReturn = future.get(SpaceSettlersSimulator.TEAM_END_ACTION_TIMEOUT, TimeUnit.MILLISECONDS);
            //finished in time
        } catch (TimeoutException e) {
            //was terminated
        	//set didReturn false
        	System.out.println(getTeamName() + " timed out in getTeamMovementEnd");
        	didReturn = false;
        } catch (InterruptedException e) {
        	//we were interrupted (should not happen but lets be good programmers) 
        	//set didReturn false
        	didReturn = false;
			e.printStackTrace();
		} catch (ExecutionException e) {
			//the executor threw and exception (should not happen but lets be good programmers) 
			//set didReturn false
        	didReturn = false;
			e.printStackTrace();
		} catch (RejectedExecutionException e) {
			System.out.println("exec" + executor.isTerminated());
			e.printStackTrace();
		}catch (Exception e) {
			// we shouldn't do this but it seems necessary to make
			// the agent behave (do nothing) if it crashes
        	didReturn = false;
			e.printStackTrace();
		}

        executor.shutdownNow();
		
		// figure out how many beacons the team has collected
		int beacons = 0;
		for (Ship ship : teamShips) {
			beacons += ship.getNumBeacons();
		}
		setTotalBeacons(beacons);
	}
	
	/**
	 * Ask the team if they want to purchase anything this turn.  You can only 
	 * purchase one item per turn and only if you have enough resourcesAvailable.
	 * 
	 * @param space
	 * @return
	 */
	public Map<UUID,SpaceSettlersPurchaseEnum> getTeamPurchases(Toroidal2DPhysics space) {
        Map<UUID,SpaceSettlersPurchaseEnum> purchase = new HashMap<UUID,SpaceSettlersPurchaseEnum>();

		final Toroidal2DPhysics clonedSpace = space.deepClone();
		final Set<SpaceSettlersActionableObject> clonedActionableObjects = getTeamActionableObjectsClone(space);
		final Map<SpaceSettlersPurchaseEnum, Integer> clonedPurchaseCost = getPurchaseCostClone();
		
        // if the previous thread call hasn't finished, then just return default
		if (executor == null || executor.isTerminated()) {
			executor = Executors.newSingleThreadExecutor();
		} else {
			return purchase;
		}

		//System.out.println("exec " + executor.isTerminated());
        Future<Map<UUID,SpaceSettlersPurchaseEnum>> future = executor.submit(
        		new Callable<Map<UUID,SpaceSettlersPurchaseEnum>>(){
        			public Map<UUID,SpaceSettlersPurchaseEnum> call() throws Exception {
        				return teamClient.getTeamPurchases(clonedSpace, 
        						clonedActionableObjects, availableMoney, clonedPurchaseCost);
        			}
        		});
        
        try {
            //start
        	purchase = future.get(SpaceSettlersSimulator.TEAM_ACTION_TIMEOUT, TimeUnit.MILLISECONDS);
            //finished in time
        } catch (TimeoutException e) {
            //was terminated
        	//return empty map, don't buy anything
        	System.out.println(getTeamName() + " timed out in getTeamPurchases");
        	purchase = new HashMap<UUID,SpaceSettlersPurchaseEnum>();
        } catch (InterruptedException e) {
        	//we were interrupted (should not happen but lets be good programmers) 
        	//return empty map, don't buy anything
        	purchase = new HashMap<UUID,SpaceSettlersPurchaseEnum>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			//the executor threw and exception (should not happen but lets be good programmers) 
        	//return empty map, don't buy anything
        	purchase = new HashMap<UUID,SpaceSettlersPurchaseEnum>();
			e.printStackTrace();
		} catch (RejectedExecutionException e) {
			System.out.println("exec" + executor.isTerminated());
			e.printStackTrace();
		}

        executor.shutdownNow();
        
        return purchase;
	}

	/**
	 * Clones the purchase cost map so the client can't modify it
	 * @return
	 */
	private Map<SpaceSettlersPurchaseEnum, Integer> getPurchaseCostClone() {
		Map<SpaceSettlersPurchaseEnum, Integer> clonedCost = new HashMap<SpaceSettlersPurchaseEnum, Integer>();
		clonedCost.putAll(costToPurchase);	
		return clonedCost;
	}

	/**
	 * Get the weapons or power ups for the team this turn
	 * 
	 * @param space
	 * @return
	 */
	public Map<UUID, SpaceSettlersPowerupEnum> getTeamPowerups(Toroidal2DPhysics space) {
        Map<UUID, SpaceSettlersPowerupEnum> powerups = new HashMap<UUID,SpaceSettlersPowerupEnum>();

		final Toroidal2DPhysics clonedSpace = space.deepClone();
		final Set<SpaceSettlersActionableObject> clonedActionableObjects = getTeamActionableObjectsClone(space);
		
        // if the previous thread call hasn't finished, then just return default
		if (executor == null || executor.isTerminated()) {
			executor = Executors.newSingleThreadExecutor();
		} else {
			return powerups;
		}

		//System.out.println("exec " + executor.isTerminated());
        Future<Map<UUID,SpaceSettlersPowerupEnum>> future = executor.submit(
        		new Callable<Map<UUID,SpaceSettlersPowerupEnum>>(){
        			public Map<UUID,SpaceSettlersPowerupEnum> call() throws Exception {
        				return teamClient.getPowerups(clonedSpace, clonedActionableObjects);
        			}
        		});
        
        try {
            //start
        	powerups = future.get(SpaceSettlersSimulator.TEAM_ACTION_TIMEOUT, TimeUnit.MILLISECONDS);
            //finished in time
        } catch (TimeoutException e) {
            //was terminated
        	//return empty map, don't buy anything
        	System.out.println(getTeamName() + " timed out in getTeamPowerups");
        	powerups = new HashMap<UUID,SpaceSettlersPowerupEnum>();
        } catch (InterruptedException e) {
        	//we were interrupted (should not happen but lets be good programmers) 
        	//return empty map, don't buy anything
        	powerups = new HashMap<UUID,SpaceSettlersPowerupEnum>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			//the executor threw and exception (should not happen but lets be good programmers) 
        	//return empty map, don't buy anything
			powerups = new HashMap<UUID,SpaceSettlersPowerupEnum>();
			e.printStackTrace();
		}

        executor.shutdownNow();
        
        return powerups;
	}


	/**
	 * Get any graphics the team client wants to draw
	 * 
	 * @return  
	 */
	public Set<SpacewarGraphics> getGraphics() {
        Set<SpacewarGraphics> graphics = new HashSet<SpacewarGraphics>();

        // if the previous thread call hasn't finished, then just return default
		if (executor == null || executor.isTerminated()) {
			executor = Executors.newSingleThreadExecutor();
		} else {
			return graphics;
		}

		Future<Set<SpacewarGraphics>> future = executor.submit(
        		new Callable<Set<SpacewarGraphics>>(){
        			public Set<SpacewarGraphics> call() throws Exception {
        				return teamClient.getGraphics();
        			}
        		});
        
        try {
            //start
        	graphics = future.get(SpaceSettlersSimulator.TEAM_GRAPHICS_TIMEOUT, TimeUnit.MILLISECONDS);
            //finished in time
        } catch (TimeoutException e) {
            //was terminated
        	//set empty array of graphics
        	System.out.println(getTeamName() + " timed out in getTeamGraphics");
        	graphics = new HashSet<SpacewarGraphics>();
        } catch (InterruptedException e) {
        	//we were interrupted (should not happen but lets be good programmers) 
        	//set empty array of graphics
        	graphics = new HashSet<SpacewarGraphics>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			//the executor threw and exception (should not happen but lets be good programmers) 
			//set empty array of graphics
        	graphics = new HashSet<SpacewarGraphics>();
			e.printStackTrace();
		}

        executor.shutdownNow();
        
		return graphics;
	}

	/**
	 * If the client wants to take input from the keyboard, they override this
	 * inside the client to return a proper key listener.  It has to be pushed up
	 * here to avoid the exploits.
	 * 
	 * @return a valid KeyAdapter
	 */
	public KeyAdapter getKeyAdapter() {
		return teamClient.getKeyAdapter();
	}

	/**
	 * If the client wants to take input from the mouse, they override this inside
	 * the client to return a proper mouse listener.  It has to be pushed up
	 * here to avoid the exploits.
	 * 
	 * @return a valid MouseAdapter
	 */
	public MouseAdapter getMouseAdapter() {
		return teamClient.getMouseAdapter();
	}
	

	
	
	/**
	 * Get the current team score
	 * @return
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Set the current team score
	 * @param score
	 */
	public void setScore(double score) {
		this.score = score;
	}
	
	public void incrementTotalMoney(double difference) {
		totalMoney += difference;
	}

	public int getTotalMoney() {
		return totalMoney;
	}
	
	public void incrementAvailableMoney(double difference) {
		availableMoney += difference;
	}

	public int getAvailableMoney() {
		return availableMoney;
	}

	public int getTotalBeacons() {
		return totalBeacons;
	}

	public void setTotalBeacons(int totalBeacons) {
		this.totalBeacons = totalBeacons;
	}
	
	public void incrementTotalBeacons() {
		this.totalBeacons++;
	}
	
	

	public String getLadderName() {
		return ladderName;
	}

	/**
	 * Called at the end of a simulation to cleanup the clients
	 */
	public void shutdownClients(Toroidal2DPhysics space) {
		teamClient.shutDown(space.deepClone());
	}

}
