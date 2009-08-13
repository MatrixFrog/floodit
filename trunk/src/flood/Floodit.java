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
import javax.swing.UIManager;

/**
 * An attempt to clone the iPhone game Floodit. See http://code.google.com/p/floodit/
 */
public class Floodit extends JFrame {
  static final boolean DEBUG = true;

  private Grid grid;
  private int numMoves = 0;

  private JPanel panel = new JPanel(new GridBagLayout());
  private JPanel buttonPanel = new JPanel(new GridBagLayout());;
  private JLabel numMovesLabel = new JLabel("0", JLabel.LEFT);
  private Canvas canvas;

  private List<SelectColorAction> allSelectColorActions = new ArrayList<SelectColorAction>();

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      /* Just use the ugly Swing look and feel =( */
    }
    new Floodit();
  }

  public Floodit() {
    super("FloodIt");
    if (DEBUG) {
      newGame(GameSettings.get("Novice"));
    }
    else {
      newGame(GameSettings.get("Beginner"));
    }
    addNumMovesLabel();
    addCanvas();
    addButtonPanel();
    addMenuBar();

    this.setSize(1100, 900);
    this.add(panel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    update();
  }

  public void undo() {
    // TODO
    numMoves--;
    update();
  }

  public void redo() {
    // TODO
    numMoves++;
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
      @Override
      public void paint(Graphics g) {
        grid.paint(g, getWidth(), getHeight());
      }
    };
    canvas.setSize(750, 750);
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.weightx = 5;
    constraints.fill = GridBagConstraints.CENTER;
    panel.add(canvas, constraints);
  }

  private void addButtonPanel() {
    GridBagConstraints constraints = new GridBagConstraints();
    // constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 1;
    constraints.gridy = 1;
    // constraints.gridwidth = 2;
    panel.add(buttonPanel, constraints);
  }

  private void addMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    gameMenu.add(new JMenuItem(new NewGameAction(this)));
    gameMenu.add(new JMenuItem(new UndoAction(this)));
    gameMenu.add(new JMenuItem(new RedoAction(this)));
    gameMenu.add("High scores");

    JMenu helpMenu = new JMenu("Help");
    helpMenu.add(new HelpAction(this));
    helpMenu.add(new AboutAction(this));

    menuBar.add(gameMenu);
    menuBar.add(helpMenu);

    this.setJMenuBar(menuBar);
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
        @Override
        public void setEnabled(boolean enabled) {
          super.setEnabled(enabled);
          if (enabled) {
            setBackground(color);
          } else {
            setBackground(Color.gray);
          }
        }
      };

      button.setFont(new Font("Dialog", Font.BOLD, 16));

      // Make keyboard shortcuts work
      button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
          KeyStroke.getKeyStroke(name), name);
      button.getActionMap().put(name, action);

      buttonPanel.add(button, constraints);
      constraints.gridx++;
    }
  }

  public void newGame(GameSettings settings) {
    newGame(new Dimension(settings.width, settings.height), settings.numColors);
  }

  public void newGame(Dimension gridSize, int numColors) {
    grid = new Grid(gridSize, numColors);
    numMoves = 0;
    allSelectColorActions.clear();
    updateButtons();
  }

  public void update() {
    numMovesLabel.setText(Integer.toString(numMoves));
    canvas.repaint();
    panel.validate();
    if (grid.isAllSameColor()) {
      displayWinMessage();
    }
    updateSelectColorActionsEnabled();
    if (DEBUG) {
      System.out.println("===== Start debug info =====");
      System.out.println("current grid: \n" + grid);
      System.out.println("===== End debug info =====");
    }
  }

  private void updateSelectColorActionsEnabled() {
    for (SelectColorAction sca : allSelectColorActions) {
      Color color = sca.color;
        sca.setEnabled(grid.containsColor(color) && !grid.getUpperLeftColor().equals(color));
    }
  }

  private void displayWinMessage() {
    Object[] options = {
        "New game", "Exit"
    };
    int option = JOptionPane.showOptionDialog(this, "You win with " + numMoves
        + " moves!", "Congratulations", JOptionPane.YES_NO_OPTION,
        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    if (DEBUG) {
      System.out.println("option=" + option);
      System.out.println("yes=" + JOptionPane.YES_OPTION);
      System.out.println("no=" + JOptionPane.NO_OPTION);
    }
    switch (option) {
    case JOptionPane.YES_OPTION:
      new NewGameDialog(this);
      update();
      break;
    case JOptionPane.NO_OPTION:
      close();
      break;
    default:
      // Do nothing
    }
  }

  public void close() {
    System.exit(0);
  }

  static class UndoAction extends AbstractAction {

    private Floodit floodit;

    public UndoAction(Floodit floodit) {
      super("Undo");
      this.floodit = floodit;
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
          ActionEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      floodit.undo();
    }
  }

  static class RedoAction extends AbstractAction {

    private Floodit floodit;

    public RedoAction(Floodit floodit) {
      super("Redo");
      this.floodit = floodit;
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y,
          ActionEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      floodit.redo();
    }
  }

  // TODO separate out the enabling/disabling functionality for better integration with undo/redo
  static class SelectColorAction extends AbstractAction {
    private Color color;
    private Floodit floodit;

    public SelectColorAction(Floodit floodit, Color color) {
      super(Square.colorsNames().get(color).toString().toUpperCase());
      this.floodit = floodit;
      this.color = color;
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Square.colorsNames()
          .get(color), KeyEvent.CTRL_MASK));
      floodit.allSelectColorActions.add(this);
      if (color.equals(floodit.grid.getUpperLeftColor())) {
        setEnabled(false);
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      floodit.grid.changeUpperLeftGroupToColor(color);
      floodit.numMoves++;
      floodit.update();
      for (SelectColorAction action : floodit.allSelectColorActions) {
        action.setEnabled(true);
      }
      this.setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enable) {
      if (enable && floodit.grid.containsColor(color)) {
        super.setEnabled(true);
      }
      else {
        super.setEnabled(false);
      }
    }
  }

}