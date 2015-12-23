package spacesettlers.clients;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import spacesettlers.actions.DoNothingAction;
import spacesettlers.actions.SpaceSettlersAction;
import spacesettlers.actions.SpaceSettlersPurchaseEnum;
import spacesettlers.graphics.SpacewarGraphics;
import spacesettlers.objects.SpaceSettlersActionableObject;
import spacesettlers.objects.SpaceSettlersObject;
import spacesettlers.powerups.SpaceSettlersPowerupEnum;
import spacesettlers.simulator.Toroidal2DPhysics;
/**
 * Client that literally never moves (not a horrible strategy if you just want to never die)
 * @author amy
 *
 */
public class DoNothingTeamClient extends TeamClient {
	@Override
	public void initialize(Toroidal2DPhysics space) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutDown(Toroidal2DPhysics space) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<UUID, SpaceSettlersAction> getMovementStart(Toroidal2DPhysics space,
			Set<SpaceSettlersActionableObject> actionableObjects) {
		HashMap<UUID, SpaceSettlersAction> actions = new HashMap<UUID, SpaceSettlersAction>();
		for (SpaceSettlersObject actionable : actionableObjects) {
				actions.put(actionable.getId(), new DoNothingAction());
		}
		return actions;
	}

	@Override
	public void getMovementEnd(Toroidal2DPhysics space, Set<SpaceSettlersActionableObject> actionableObjects) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<SpacewarGraphics> getGraphics() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	/**
	 * Do nothing never purchases
	 */
	public Map<UUID, SpaceSettlersPurchaseEnum> getTeamPurchases(Toroidal2DPhysics space,
			Set<SpaceSettlersActionableObject> actionableObjects, int availableMoney, Map<SpaceSettlersPurchaseEnum, Integer> purchaseCosts) {
		// TODO Auto-generated method stub
		return new HashMap<UUID,SpaceSettlersPurchaseEnum>();
	}

	@Override
	public Map<UUID, SpaceSettlersPowerupEnum> getPowerups(Toroidal2DPhysics space,
			Set<SpaceSettlersActionableObject> actionableObjects) {
		// TODO Auto-generated method stub
		return null;
	}

}
