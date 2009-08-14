package flood;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import common.swingutils.SwingUtils;

public class NewGameDialog extends JDialog {

  private JPanel panel = new JPanel(new GridBagLayout());
  GridBagConstraints constraints = new GridBagConstraints();

  public NewGameDialog(Floodit owner) {
    super(owner, "New Game", true);
    this.add(panel);

    initConstraints();

    for (JLabel label : headers()) {
      panel.add(label, constraints);
      constraints.gridx++;
    }

    constraints.gridy++;
    for (String gameType : new String[] {"Beginner", "Intermediate", "Advanced"}) {
      constraints.gridx=0;
      addRow(gameType, false);
      constraints.gridy++;
    }
    constraints.gridx=0;
    addRow("Custom", true);
    constraints.gridy++;

    constraints.gridx = 0;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.anchor = GridBagConstraints.CENTER;
    constraints.fill = GridBagConstraints.NONE;
    panel.add(new JButton(new AbstractAction() {
      {
        putValue(NAME, "Cancel");
      }
      @Override public void actionPerformed(ActionEvent arg0) {
        // TODO (maybe) have this do something other than close the entire game
        ((Floodit) NewGameDialog.this.getOwner()).close();
      }
    }), constraints);

    this.setSize(this.getPreferredSize());
    SwingUtils.centerOnScreen(this);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }

  private void initConstraints() {
    constraints.insets = new Insets(20, 10, 20, 10);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.ipadx = 30;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.CENTER;
  }

  /**
   * @param gameTypeName The type of game. Should be in GameSettings.gameTypes.keySet()
   * @param constraints The constraints that will be used to add the row (with the exception
   * of .gridx which obviously will vary
   */
  private void addRow(String gameTypeName, boolean enable) {
    final GameSettings settings = GameSettings.get(gameTypeName);

    constraints.gridx = 0;
    JButton button = new JButton(gameTypeName);
    button.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        ((Floodit) getOwner()).newGame(settings);
        ((Floodit) getOwner()).update();
        NewGameDialog.this.dispose();
      }
    });
    panel.add(button, constraints);

    constraints.gridx++;
    JFormattedTextField widthField =
      new JFormattedTextField(NumberFormat.getNumberInstance());
    widthField.setText(Integer.toString(settings.width));
    widthField.getDocument().addDocumentListener(new DocumentListener() {
      @Override public void changedUpdate(DocumentEvent e) {
        throw new RuntimeException();
      }
      @Override public void insertUpdate(DocumentEvent e) {
        handleChange(e);
      }
      @Override public void removeUpdate(DocumentEvent e) {
        handleChange(e);
      }
      private void handleChange(DocumentEvent e) {
        try {
          settings.width = Integer.parseInt(
              e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (NumberFormatException f) {
          settings.width = 0;
        } catch (BadLocationException f) {
          throw new RuntimeException(f);
        }
      }
    });
    widthField.setEnabled(enable);
    panel.add(widthField, constraints);

    constraints.gridx++;
    JFormattedTextField heightField =
      new JFormattedTextField(Integer.toString(settings.height));
    heightField.getDocument().addDocumentListener(new DocumentListener() {
      @Override public void changedUpdate(DocumentEvent e) {
        throw new RuntimeException();
      }
      @Override public void insertUpdate(DocumentEvent e) {
        handleChange(e);
      }
      @Override public void removeUpdate(DocumentEvent e) {
        handleChange(e);
      }
      private void handleChange(DocumentEvent e) {
        try {
          settings.height = Integer.parseInt(
              e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (NumberFormatException f) {
          settings.height = 0;
        } catch (BadLocationException f) {
          throw new RuntimeException(f);
        }
      }
    });
    heightField.setEnabled(enable);
    panel.add(heightField, constraints);

    constraints.gridx++;
    JSpinner numColors = new JSpinner(
        new SpinnerNumberModel(settings.numColors, 2, Square.colors().size(), 1));
    numColors.addChangeListener(new ChangeListener() {
      @Override public void stateChanged(ChangeEvent e) {
        settings.numColors = (Integer) ((JSpinner) e.getSource()).getValue();
      }
    });
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
