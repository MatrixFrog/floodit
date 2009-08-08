package flood;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * An attempt to clone the iPhone game Floodit
 */
public class Floodit {

	static final boolean DEBUG = true;

	private Grid grid;
	private int numMoves = 0;

	private JFrame window = new JFrame("Flood It");
	private JPanel panel = new JPanel(new GridBagLayout());
	private JPanel buttonPanel = new JPanel(new GridBagLayout());;
	private JLabel numMovesLabel = new JLabel("0", JLabel.LEFT);
	private Canvas canvas;

	private List<SelectColorAction> allSelectColorActions =
		new ArrayList<SelectColorAction>();

	public Floodit() {
		newGame();

		addNumMovesLabel();
		addCanvas();
		addButtonPanel();
		addMenuBar();

		window.setSize(1100, 900);
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		update();
	}

	private void addNumMovesLabel() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		numMovesLabel.setFont(new Font("Dialog", Font.BOLD, 34));
		panel.add(numMovesLabel, constraints);
	}

	private void addCanvas() {
		GridBagConstraints constraints = new GridBagConstraints();
		canvas = new Canvas() {
			@Override public void paint(Graphics g) {
				grid.paint(g, getWidth(), getHeight());
			}
		};
		canvas.setSize(750, 750);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 5;
		panel.add(canvas, constraints);
	}

	private void addButtonPanel() {
		GridBagConstraints constraints = new GridBagConstraints();
		//constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 1;
		//constraints.gridwidth = 2;
		panel.add(buttonPanel, constraints);
	}

	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.add(new JMenuItem(new NewGameAction(this)));
		gameMenu.add("Undo");
		gameMenu.add("Redo");
		gameMenu.add("High scores");

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add("Instructions");
		helpMenu.add("About");

		menuBar.add(gameMenu);
		menuBar.add(helpMenu);

		window.setJMenuBar(menuBar);
	}

	private void updateButtons() {
		buttonPanel.removeAll();

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.ipadx = 20;
		constraints.ipady = 20;
		constraints.insets = new Insets(10, 10, 10, 10);

		for (final Color color : grid.getColors()) {
			char name = Square.getName(color);
			SelectColorAction action = new SelectColorAction(this, color);
			JButton button = new JButton(action) {
				@Override public void setEnabled(boolean enabled) {
					super.setEnabled(enabled);
					if (enabled) {
						setBackground(color);
					} else {
						setBackground(Color.gray);
					}
				}
			};

			button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
					KeyStroke.getKeyStroke(name), name);
			button.getActionMap().put(name, action);

			buttonPanel.add(button, constraints);
			constraints.gridx++;
		}
	}

	public void newGame(Dimension gridSize, int numColors) {
		grid = new Grid(gridSize, numColors);
		numMoves = 0;
		allSelectColorActions.clear();
		updateButtons();
		//update();
	}

	public void newGame() {
		// TODO display a little window that lets you choose what kind of game you want
		// For now, just hard-code a default value.
		newGame(new Dimension(10, 10), 4);
	}

	public static void main(String[] args) {
		new Floodit();
	}

	public void update() {
		numMovesLabel.setText(Integer.toString(numMoves));
		canvas.repaint();
		panel.validate();
		if (grid.isAllSameColor()) {
			displayWinMessage();
			newGame();
			update();
		}
	}

	private void displayWinMessage() {
		JOptionPane.showMessageDialog(window, "You win with " + numMoves + " moves!",
				"Congratulations", JOptionPane.PLAIN_MESSAGE);
	}

	static class NewGameAction extends AbstractAction {
		private Floodit floodit;

		public NewGameAction(Floodit floodit) {
			super("New game...");
			this.floodit = floodit;
	        putValue(SHORT_DESCRIPTION, "Start a new game.");
	        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
	                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		}

		@Override public void actionPerformed(ActionEvent e) {
			floodit.newGame();
			floodit.update();
		}
	}

	class SelectColorAction extends AbstractAction {
		private Color color;
		private Floodit floodit;

		public SelectColorAction(Floodit floodit, Color color) {
			super(Square.colorsNames().get(color).toString());
			this.floodit = floodit;
			this.color = color;
			//putValue(ACTION_COMMAND_KEY, Square.colorsNames().get(color).toString());
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					Square.colorsNames().get(color), KeyEvent.CTRL_MASK));
			floodit.allSelectColorActions.add(this);
			if (color.equals(floodit.grid.getUpperLeftColor())) {
				setEnabled(false);
			}
		}

		@Override public void actionPerformed(ActionEvent e) {
			for (SelectColorAction action : floodit.allSelectColorActions) {
				action.setEnabled(true);
			}
			this.setEnabled(false);
			floodit.grid.changeUpperLeftGroupToColor(color);
			floodit.numMoves++;
			floodit.update();
		}
	}
}