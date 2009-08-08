package flood;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class NewGameDialog extends JDialog {

  private JPanel panel = new JPanel(new GridBagLayout());
  private JTextField width = new JTextField(10);
  private JTextField height = new JTextField(10);
  private JSpinner numColors = new JSpinner();

  // unused
  private JPanel widthPanel = labeledTextFieldPanel("Width: ", width);
  private JPanel heightPanel = labeledTextFieldPanel("Height: ", height);

  public NewGameDialog(JFrame owner) {
    super(owner, "New Game");
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

    constraints.gridx=0;
    constraints.gridy=1;
    for (JButton button : buttons()) {
      panel.add(button, constraints);
      constraints.gridy++;
    }
    constraints.gridy--;

    constraints.gridx++;
    panel.add(width, constraints);

    constraints.gridx++;
    panel.add(height, constraints);

    constraints.gridx++;
    constraints.fill = GridBagConstraints.NONE;
    panel.add(numColors, constraints);

    this.setSize(this.getPreferredSize());
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  // unused
  private JPanel labeledTextFieldPanel(String label, JTextField textField) {
    JPanel labeledTextField = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.weightx = 1;
    constraints.gridx = 0;
    labeledTextField.add(new JLabel(label), constraints);

    constraints.weightx = 200;
    constraints.gridx++;
    labeledTextField.add(textField, constraints);
    return labeledTextField;
  }

  private List<JButton> buttons() {
    List<JButton> buttons = Arrays.asList(
        new JButton("Beginner"),
        new JButton("Intermediate"),
        new JButton("Advanced"),
        new JButton("Custom")
    );
    return buttons;
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

  /**
   * For testing only
   */
  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    new NewGameDialog(null);
  }
}
