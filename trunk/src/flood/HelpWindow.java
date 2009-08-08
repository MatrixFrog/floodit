package flood;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

class HelpWindow extends JDialog {

  public static void main(String[] args) {
    new HelpWindow(null, "help.htm");
  }

  JLabel label;

  HelpWindow(JFrame owner, String helpFilename) {
    super(owner);
    setSize(350, 250);

    label = new JLabel();
    this.add(label);

    String helpText;
    try {
      InputStream helpStream = getClass().getResourceAsStream(helpFilename);
      if (helpStream == null) {
        JOptionPane.showMessageDialog(owner, "ERROR: Could not load '"
            + helpFilename + "'");
        return;
      }
      helpText = IOUtils.toString(helpStream);

    } catch (IOException e) {
      helpText = e.toString();
    }
    label.setText(helpText);

    this.setVisible(true);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

}
