package spacesettlers.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import spacesettlers.clients.Team;
import spacesettlers.configs.SpacewarConfig;
import spacesettlers.simulator.SpaceSettlersSimulator;

/**
 * Main GUI for the Space Settlers simulator
 * @author amy
 *
 */
public class SpaceSettlersGUI {
	JFrame mainFrame;
	
	JSpaceSettlersComponent mainComponent;
	
	JSpaceSettlersInfoComponent infoComponent;
	
	JPanel infoPanel, mainPanel;
	
	boolean isPaused = false;
	
	SpaceSettlersSimulator spacewarSimulator;
	
	/**
	 * Make a new GUI
	 * @param config
	 */
	public SpaceSettlersGUI(SpacewarConfig config, SpaceSettlersSimulator spacewarSimulator) {
		super();
		this.spacewarSimulator = spacewarSimulator;
		
		mainFrame = new JFrame("Spacewar 2");
		
		infoPanel = new JPanel();
		mainPanel = new JPanel();
		
		infoComponent = new JSpaceSettlersInfoComponent(config.getWidth());
		mainComponent = new JSpaceSettlersComponent(config.getHeight(), config.getWidth());

		infoPanel.add(infoComponent);
		infoPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));

		// add the labels (we will change the text later)
//        JLabel label = new JLabel("Timestep: ");
//        label.setBackground(Color.GREEN);
//        
//        infoPanel.add(label);
        

		//infoPanel.add(Box.createRigidArea(new Dimension(0,5)));
		mainPanel.add(mainComponent);
		MyEmptyBorder myEmptyBorder = new MyEmptyBorder();
		mainPanel.setBorder(myEmptyBorder);
		
		Container contentPanel = mainFrame.getContentPane();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		contentPanel.add(infoPanel);
		contentPanel.add(mainPanel);
		//contentPanel.setPreferredSize(new Dimension(config.getWidth(), config.getHeight() + infoComponent.getHeight()));
		//contentPanel.setMinimumSize(new Dimension(config.getWidth(), config.getHeight() + infoComponent.getHeight()));
		
		// create a help menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Help");
		menuBar.add(menu);
		JMenuItem item = new JMenuItem("Interface help");
		item.setAccelerator(KeyStroke.getKeyStroke('h'));
		menu.add(item);
		item.addActionListener(new HelpMenuListener());
		
		mainFrame.setJMenuBar(menuBar);
		
		// add a key listener to handle pauses
		mainFrame.addKeyListener(new KeyAdapter() {
			/**
			 * Listen to the GUI level key commands
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				char key = e.getKeyChar();
				if (key == 'p' || key == 'P') {
					togglePause();
				} else if (key == '+') {
					doubleSpeed();
				} else if (key == '-') {
					slowSpeed();
				}
			}
		}
		);
		
		// add any client key & mouse listeners that want to be added
		for (Team team : spacewarSimulator.getTeams()) {
			KeyAdapter listener = team.getKeyAdapter();
			if (listener != null) {
				mainFrame.addKeyListener(listener);
			}

			MouseAdapter mouseListen = team.getMouseAdapter();
			if (listener != null) {
				mainPanel.addMouseListener(mouseListen);
				mainPanel.addMouseMotionListener(mouseListen);
			}

		}

		// finally draw it
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setVisible(true);
		//System.out.println("Info component width x height " + infoPanel.getWidth() + " x " + infoPanel.getHeight());
		//System.out.println("Main component width x height " + mainPanel.getWidth() + " x " + mainPanel.getHeight());
	}

	/**
	 * Toggle the paused state
	 */
	protected void togglePause() {
		isPaused = !isPaused;
		spacewarSimulator.setPaused(isPaused);

		if (isPaused) {
			System.out.println("Pausing spacewar");
		} else {
			System.out.println("Unpausing spacewar");
		}
		
	}

	/**
	 * Double the sim speed (by halving graphics sleep)
	 */
	protected void doubleSpeed() {
		int newSpeed = Math.max(5, spacewarSimulator.getGraphicsSleep() / 2);
		spacewarSimulator.setGraphicsSleep(newSpeed);
	}

	/**
	 * Slow the sim speed (by doubling graphics sleep)
	 */
	protected void slowSpeed() {
		int newSpeed = Math.min(spacewarSimulator.getGraphicsSleep() * 2, 240);
		spacewarSimulator.setGraphicsSleep(newSpeed);
	}

	
	//	/**
//	 * @return the main graphics component for the GUI so ships and the like can be drawn on it
//	 */
//	public Graphics2D getGraphics() {
//		return (Graphics2D) mainComponent.getGraphics();
//	}
//
	/**
	 * Redraws the graphics
	 * @param spacewarSimulator
	 */
	public void redraw() {
		infoComponent.setSimulator(spacewarSimulator);
		//mainFrame.paintComponents(getGraphics());
		mainComponent.setSimulator(spacewarSimulator);
		mainFrame.repaint();
	}
	
	/**
	 * Listener for the help menu
	 * @author amy
	 *
	 */
	public class HelpMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			JFrame helpFrame = new JFrame("Keyboard and Mouse commands");
			
			String helpText = "Summary of all the keyboard commands.\n\n\n";
			helpText += " Main GUI commands:\n";
			helpText += "p/P  pauses and unpauses the simulation\n";
			helpText += "+  speeds up the simulation\n";
			helpText += "-  slows down the simulation\n";
			helpText += "h  brings up this menu\n\n";

			helpText +=	"Summary of the commands for the keyboard and mouse for the human client.\n\n\n";
			helpText += " Keyboard commands:\n";
			helpText += " Use the arrow keys to move in the associated direction.  Note that they give you acceleration in the direction of the arrow.\n";
			helpText += " The space bar will fire missiles.\n\n";
			helpText += " Mouse commands;\n";
			helpText += " Click anywhere in the GUI to have your agent fly to that location.  Don't forget the world is toroidal!";
			JTextArea helpTextArea = new JTextArea(helpText);
			helpTextArea.setEditable(false);
			
			helpFrame.add(helpTextArea);
			
			helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			helpFrame.setResizable(false);
			helpFrame.pack();
			helpFrame.setVisible(true);


		}

	}




}
