package spacesettlers.gui;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import spacesettlers.clients.Team;
import spacesettlers.objects.resources.ResourcePile;
import spacesettlers.objects.resources.ResourceTypes;
import spacesettlers.simulator.SpaceSettlersSimulator;

/**
 * Displays the resources in a nice gridded format for a team
 * 
 * @author amy
 */
public class ResourcesPanel extends JPanel {
	JLabel waterAvail, waterTotal, fuelAvail, fuelTotal, metalsAvail, metalsTotal;

	public ResourcesPanel() {
		setLayout(new GridLayout(4,3));
		
		// row 1: the titles
		JLabel resources = new JLabel("Resources");
		add(resources);

		JLabel avail = new JLabel("Available");
		add(avail);

		JLabel total = new JLabel("Total");
		add(total);
		
		// the data: next row is water
		JLabel water = new JLabel("Water: ");
		add(water);
		
		waterAvail = new JLabel("Foo");
		add(waterAvail);
		
		waterTotal = new JLabel("Foo");
		add(waterTotal);

		// fuel row
		JLabel fuel = new JLabel("Fuel: ");
		add(fuel);

		fuelAvail = new JLabel("Foo");
		add(fuelAvail);
		
		fuelTotal = new JLabel("Foo");
		add(fuelTotal);
		
		// metals row
		JLabel metals = new JLabel("Metals: ");
		add(metals);

		metalsAvail = new JLabel("Foo");
		add(metalsAvail);
		
		metalsTotal = new JLabel("Foo");
		add(metalsTotal);
	}
	
	public void updateData(SpaceSettlersSimulator simulator, String teamName) {
		Team team = null;
		for (Team curTeam : simulator.getTeams()) {
			if (curTeam.getLadderName().equalsIgnoreCase(teamName)) {
				team = curTeam;
				break;
			}
		}
		
		ResourcePile avail = team.getAvailableResources();
		ResourcePile total = team.getTotalResources();
		
		waterAvail.setText("" + avail.getResourceQuantity(ResourceTypes.WATER));
		fuelAvail.setText("" + avail.getResourceQuantity(ResourceTypes.FUEL));
		metalsAvail.setText("" + avail.getResourceQuantity(ResourceTypes.METALS));
		waterTotal.setText("" + total.getResourceQuantity(ResourceTypes.WATER));
		fuelTotal.setText("" + total.getResourceQuantity(ResourceTypes.FUEL));
		metalsTotal.setText("" + total.getResourceQuantity(ResourceTypes.METALS));
		
	}
	
}
