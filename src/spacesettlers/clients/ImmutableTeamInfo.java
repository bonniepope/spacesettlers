package spacesettlers.clients;

/**
 * Immutable class that holds the necessary team info to share with
 * other teams.
 * 
 * @author amy
 *
 */
public class ImmutableTeamInfo {
	/**
	 * current team score (set in the simulator, which knows how the team is being scored)
	 */
	double score;

	/**
	 * Name of the team 
	 */
	String teamName;
	
	/**
	 * available (unspent) money from the asteroids and the total money earned
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

	public ImmutableTeamInfo(Team team) {
		score = team.score;
		teamName = team.teamName;
		availableMoney = team.availableMoney;
		totalMoney = team.totalMoney;
		totalBeacons = team.totalBeacons;
		ladderName = team.ladderName;
	}

	public double getScore() {
		return score;
	}

	public String getTeamName() {
		return teamName;
	}

	public int getAvailableMoney() {
		return availableMoney;
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public int getTotalBeacons() {
		return totalBeacons;
	}

	public String getLadderName() {
		return ladderName;
	}

}
