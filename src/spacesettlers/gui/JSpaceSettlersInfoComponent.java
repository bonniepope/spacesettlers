package spacesettlers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JLabel;

import spacesettlers.clients.Team;
import spacesettlers.simulator.SpaceSettlersSimulator;

/**
 * Shows general information about the game
 * 
 * @author amy
 */
@SuppressWarnings("serial")
public class JSpaceSettlersInfoComponent extends JComponent {
	int height = 15;
	
	int width;
	
	SpaceSettlersSimulator simulator;
	
	JLabel timestepsLabel;

	public JSpaceSettlersInfoComponent(int width) {
		super();
		this.width = width;

        setPreferredSize(new Dimension(width, height));
	}
	
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

        final Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        		RenderingHints.VALUE_ANTIALIAS_ON);

        // show the current timestep
        graphics.setPaint(Color.BLACK);
        graphics.setFont(JSpaceSettlersComponent.FONT12);
        graphics.drawString("Timesteps: ", 10, 10);

        // handle an annoying race condition in the GUI
        if (simulator == null) {
        	return;
        }
        
        graphics.drawString(Integer.toString(simulator.getTimestep()), 100, 10);
        
        graphics.drawString("Speed: ", 150, 10);

        graphics.drawString(Double.toString(simulator.getGraphicsSleep()), 200, 10);

        
        // show the current score for each team
        int x = 240;
        for (Team team : simulator.getTeams()) {
        	graphics.setPaint(team.getTeamColor());
        	graphics.drawString(team.getLadderName() + ": ", x, 10);
        	x += (team.getLadderName().length() * 10);
        	
        	String score = Double.toString(team.getScore());
        	graphics.drawString(score, x, 10);
        	
        	x += (score.length() * 10);
        	
        	String available = "" + team.getAvailableMoney();
        	graphics.drawString(available, x, 10);
        	
        	x += ((available.length() + 2) * 10);
        	
        	
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
