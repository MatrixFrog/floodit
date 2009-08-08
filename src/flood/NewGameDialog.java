package flood;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class NewGameDialog extends JDialog {

  private JPanel panel = new JPanel(new GridBagLayout());
  //private JTextField width = new JTextField(10);
  //private JTextField height = new JTextField(10);
  //private JSpinner numColors = new JSpinner();

  public NewGameDialog(Floodit owner) {
    super(owner, "New Game", true);
    this.add(panel);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = new Insets(20, 10, 20, 10);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.ipadx = 30;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.CENTER;

    for (JLabel label : headers()) {
      panel.add(label, constraints);
      constraints.gridx++;
    }

    constraints.gridy++;
    constraints.gridx=0;
    for (String gameType : new String[] {"Beginner", "Intermediate", "Advanced"}) {
      addRow(gameType, constraints, false);
      constraints.gridy++;
    }
    addRow("Custom", constraints, true);

    this.setSize(this.getPreferredSize());
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Warning: This method will change the gridx attribute of the GridBigConstraints object
   * that is passed in.
   *
   * @param gameTypeName The type of game. Should be in GameSettings.gameTypes.keySet()
   * @param constraints The constraints that will be used to add the row (with the exception
   * of .gridx which obviously will vary
   */
  private void addRow(String gameTypeName, GridBagConstraints constraints, boolean enable) {
    final GameSettings settings = GameSettings.get(gameTypeName);

    constraints.gridx = 0;
    JButton button = new JButton(gameTypeName);
    button.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent arg0) {
        ((Floodit) getOwner()).newGame(settings);
        ((Floodit) getOwner()).update();
        NewGameDialog.this.dispose();
      }
    });
    panel.add(button, constraints);

    constraints.gridx++;
    JTextField widthField = new JTextField(Integer.toString(settings.width));
    widthField.setEnabled(enable);
    panel.add(widthField, constraints);

    constraints.gridx++;
    JTextField heightField = new JTextField(Integer.toString(settings.height));
    heightField.setEnabled(enable);
    panel.add(heightField, constraints);

    constraints.gridx++;
    JSpinner numColors = new JSpinner(
        new SpinnerNumberModel(settings.numColors, 2, Square.colors().size(), 1));
    numColors.setEnabled(enable);
    panel.add(numColors, constraints);
  }

  private List<JLabel> headers() {
    List<JLabel> headers = Arrays.asList(
        new JLabel("Game type"),
        new JLabel("Width"),
        new JLabel("Height"),
        new JLabel("# of Colors")
    );
    for (JLabel label : headers) {
      label.setFont(new Font("Dialog", Font.BOLD, 20));
    }
    return headers;
  }

}
