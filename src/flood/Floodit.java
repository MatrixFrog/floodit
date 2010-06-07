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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import util.swingutils.SwingUtils;
import flood.undo.UndoStack;

/**
 * An attempt to clone the iPhone game Floodit. See http://code.google.com/p/floodit/
 */
public class Floodit extends JFrame {
  static final boolean DEBUG = false;
  /** The size of the grid, in pixels */
  final Dimension gridSize = new Dimension(750, 750);
  JTextArea changeLog; // only used for debugging

  Grid grid;
  private int numMoves = 0;

  private JPanel panel = new JPanel(new GridBagLayout());
  private JPanel buttonPanel = new JPanel(new GridBagLayout());
  private JLabel numMovesLabel = new JLabel("0", JLabel.LEFT);
  private Canvas gridPanel; // TODO turn this into a JPanel so that it can be double-buffered (?)

  private UndoStack<Grid> undoStack;
  private UndoAction undoAction = new UndoAction(this);
  private RedoAction redoAction = new RedoAction(this);

  private Map<Color,SelectColorAction> selectColorActions = new HashMap<Color,SelectColorAction>();

  public static void main(String[] args) {
    SwingUtils.useDialogExceptionHandler();
    SwingUtils.useDefaultLookAndFeel();
    new Floodit();
  }

  public Floodit() {
    super("FloodIt");
    if (DEBUG) {
        changeLog = new JTextArea(5, 30);
        JDialog changeLogDialog = new JDialog(this);
        changeLogDialog.add(new JScrollPane(changeLog));
        changeLogDialog.setSize(300, 800);
        changeLogDialog.setVisible(true);
    }
    if (DEBUG) {
      newGame(GameSettings.get("Novice"));
    }
    else {
      newGame(GameSettings.get("Beginner"));
    }
    addNumMovesLabel();
    addGridPanel();
    addButtonPanel();
    addMenuBar();

    this.setSize(1100, 900);
    SwingUtils.centerOnScreen(this);
    this.add(panel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    update();
  }

  public void undo() {
    grid = undoStack.undo().clone();
    numMoves--;
    update();
  }

  public void redo() {
    grid = undoStack.redo().clone();
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

  private void addGridPanel() {
    GridBagConstraints constraints = new GridBagConstraints();
    gridPanel = new Canvas() {
      @Override
      public void paint(Graphics g) {
        grid.paint(g, getWidth(), getHeight());
      }
    };
    gridPanel.addMouseListener(new FlooditMouseListener(this));
    gridPanel.setSize(gridSize);
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.weightx = 5;
    constraints.fill = GridBagConstraints.CENTER;
    panel.add(gridPanel, constraints);
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
    gameMenu.add(new JMenuItem(undoAction));
    gameMenu.add(new JMenuItem(redoAction));
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
    undoStack = new UndoStack<Grid>();
    undoStack.add(grid.clone());
    numMoves = 0;
    selectColorActions.clear();
    updateButtons();
  }

  public void update() {
    numMovesLabel.setText(Integer.toString(numMoves));
    gridPanel.repaint();
    panel.validate();
    if (grid.isAllSameColor()) {
      displayWinMessage();
    }
    undoAction.setEnabled(undoStack.canUndo());
    redoAction.setEnabled(undoStack.canRedo());
    updateSelectColorActionsEnabled();
    if (DEBUG) {
      changeLog.append("===== Start debug info =====\n");
      changeLog.append("current grid: \n" + grid + "\n");
      changeLog.append("undoStack: " + undoStack + "\n");
      changeLog.append("===== End debug info =====\n");
    }
  }

  void selectColor(Color color) {
    SelectColorAction sca = selectColorActions.get(color);
    if (sca.isEnabled()) {
      sca.actionPerformed(null); // TODO this is a very backward way of doing this
    }
  }

  private void updateSelectColorActionsEnabled() {
    for (Entry<Color, SelectColorAction> entry : selectColorActions.entrySet()) {
      Color color = entry.getKey();
      SelectColorAction sca = entry.getValue();
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
    public void actionPerformed(ActionEvent e) {
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
    public void actionPerformed(ActionEvent e) {
      floodit.redo();
    }
  }

  static class SelectColorAction extends AbstractAction {
    private Color color;
    private Floodit floodit;

    public SelectColorAction(Floodit floodit, Color color) {
      super(Square.colorsNames().get(color).toString().toUpperCase());
      this.floodit = floodit;
      this.color = color;
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Square.colorsNames()
          .get(color), KeyEvent.CTRL_MASK));
      floodit.selectColorActions.put(color, this);
      if (color.equals(floodit.grid.getUpperLeftColor())) {
        setEnabled(false);
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      floodit.grid.changeUpperLeftGroupToColor(color);
      floodit.numMoves++;

      floodit.undoStack.add(floodit.grid.clone());
      floodit.update();
    }

  }

  void debug(Object o) {
    if (DEBUG) {
      changeLog.append(o + "\n");
    }
  }

}