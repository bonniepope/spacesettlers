package spacesettlers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import spacesettlers.clients.Team;
import spacesettlers.simulator.SpaceSettlersSimulator;

/**
 * Shows general information about the game
 * 
 * @author amy
 */
@SuppressWarnings("serial")
public class JSpaceSettlersInfoPanel extends JPanel {
	SpaceSettlersSimulator simulator;
	
	JLabel timestepData;
	
	GridBagConstraints constraints;
	
	GlobalInfoPanel globalInfo;
	
	Map<String, TeamInfoPanel> teamComponents;

	public JSpaceSettlersInfoPanel(SpaceSettlersSimulator simulator) {
		super();

		constraints = new GridBagConstraints();
		//constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(10, 10, 10, 10);
		setLayout(new GridBagLayout());

		constraints.gridx = 0;
		constraints.gridy = 0;
		globalInfo = new GlobalInfoPanel();
		add(globalInfo, constraints);
        teamComponents = new HashMap<String, TeamInfoPanel>();
        
		// populate the team hash map the first time it is called with a valid simulator object
		int teamNum = 1;
        for (Team team : simulator.getTeams()) {
        	TeamInfoPanel tInfo = new TeamInfoPanel(team);
        	teamComponents.put(team.getTeamName(), tInfo);
    		constraints.gridx = 0;
    		constraints.gridy = teamNum;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            add(tInfo, constraints);
            teamNum++;
        }
	}
	
	public void updateData() {
        globalInfo.updateData(simulator);
		
		for (TeamInfoPanel panel : teamComponents.values()) {
			panel.updateData(simulator);
		}
	}
	

	/**
	 * Save the current state of the sim for painting
	 * @param spacewarSimulator
	 */
	public void setSimulator(SpaceSettlersSimulator spacewarSimulator) {
		simulator = spacewarSimulator;
	}

	
	
}
