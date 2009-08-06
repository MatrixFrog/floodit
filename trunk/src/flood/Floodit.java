package flood;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

	protected static final boolean DEBUG = true;

	private JFrame window = new JFrame("Flood It");
	private JPanel panel;
	private Grid grid;
	private JPanel buttonPanel;
	private int numMoves = 0;
	private JLabel numMovesLabel = new JLabel("0", JLabel.LEFT);
	private JMenu gameMenu;
	private JMenu helpMenu;

	private static List<SelectColorAction> allSelectColorActions =
		new ArrayList<SelectColorAction>();

	private Canvas canvas = new Canvas() {
		@Override
		public void paint(Graphics g) {
			int squareWidth = getWidth() / grid.getWidth();
			int squareHeight = getHeight() / grid.getHeight();
			for (int i=0; i<grid.getWidth(); i++) {
				for (int j=0; j<grid.getWidth(); j++) {
					Square square = grid.get(i,j);
					boolean debugDot = DEBUG && grid.upperLeftGroupContains(square);
					square.paint(g, i * squareWidth, j * squareHeight,
							squareWidth, squareHeight, debugDot);
				}
			}
		}
	};
	public Floodit() {
		GridBagConstraints constraints;

		panel = new JPanel(new GridBagLayout());

		canvas.setSize(750, 750);

		addNumMovesLabel();

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.weightx = 5;
		panel.add(canvas, constraints);

		buttonPanel = new JPanel();
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		panel.add(buttonPanel, constraints);

		addMenuBar();
		window.setSize(1100, 900);
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		newGame();

		update();
	}

	private void addNumMovesLabel() {
		GridBagConstraints constraints;
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		numMovesLabel.setSize(100, 100);
		numMovesLabel.setFont(new Font("Dialog", Font.BOLD, 34));
		panel.add(numMovesLabel, constraints);
	}

	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		gameMenu = new JMenu("Game");
		gameMenu.add(new JMenuItem(new NewGameAction(this)));
		gameMenu.add("Undo");
		gameMenu.add("Redo");
		gameMenu.add("High scores");

		helpMenu = new JMenu("Help");
		helpMenu.add("Instructions");
		helpMenu.add("About");

		menuBar.add(gameMenu);
		menuBar.add(helpMenu);

		window.setJMenuBar(menuBar);
	}

	private void updateButtons() {
		buttonPanel.removeAll();

		int i=0;
		for (Color color : grid.colors()) {
			JButton button = new JButton(new SelectColorAction(this, color));

			button.setBackground(color);

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = i++;
			constraints.weightx = 1;
			constraints.ipady = 10;
			buttonPanel.add(button, constraints);
		}
	}

	public void newGame(Dimension gridSize, int numColors) {
		grid = new Grid(gridSize, numColors);
		numMoves = 0;
		allSelectColorActions.clear();
		updateButtons();
		update();
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
		canvas.repaint();
		numMovesLabel.setText(Integer.toString(numMoves));

		if (grid.isAllSameColor()) {
			displayWinMessage();
			newGame();
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
		}
	}

	static class SelectColorAction extends AbstractAction {
		private Color color;
		private Floodit floodit;

		public SelectColorAction(Floodit floodit, Color color) {
			super(Square.colorsNames().get(color).toString());
			this.floodit = floodit;
			this.color = color;
			putValue(ACTION_COMMAND_KEY, Square.colorsNames().get(color).toString());
			allSelectColorActions.add(this);
			if (DEBUG) {
				System.out.println(allSelectColorActions.size());
			}
		}

		@Override public void actionPerformed(ActionEvent e) {
			for (SelectColorAction action : allSelectColorActions) {
				action.setEnabled(true);
			}
			this.setEnabled(false);
			floodit.grid.changeUpperLeftGroupToColor(color);
			floodit.numMoves++;
			floodit.update();
		}
	}
}
