package flood;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * An attempt to clone the iPhone game Floodit
 */
public class Floodit extends Canvas {

	//private static final boolean DEBUG = true;
	private static final boolean DEBUG = false;

	private JPanel panel;
	private Grid grid;

	public static void main(String[] args) {
		new Floodit();
	}

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

	public void addMenuBar() {
		JMenuBar menubar = new JMenuBar();

	}

	public Floodit() {
		grid = new Grid(10, 10);
		panel = new JPanel(new GridBagLayout());
		panel.setSize(800, 800);
		//addMenuBar();

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = Square.colors().size();

		panel.add(this, constraints);
		addButtons();
		this.setSize(800, 800);
		panel.setVisible(true);

		JFrame window = new JFrame();
		window.setSize(900, 900);
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	private void addButtons() {
		JPanel buttonPanel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;
		panel.add(buttonPanel, constraints);

		int i=0;
		for (final Color color : Square.colorsNames().keySet()) {
			Character name = Square.colorsNames().get(color);
			JButton button = new JButton(name.toString());
			button.setBackground(color);
			button.setMnemonic(name);

			button.setSize(50, 50);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					grid.changeUpperLeftGroupToColor(color);
					repaint();
				}
			});
			constraints = new GridBagConstraints();
			constraints.gridx = i++;
			constraints.weightx = 1;
			constraints.ipady = 10;
			buttonPanel.add(button, constraints);
		}
	}
}
