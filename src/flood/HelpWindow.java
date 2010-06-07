package flood;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

class HelpWindow extends JDialog {

  JLabel label;

  HelpWindow(JFrame owner, String helpFilename) {
    super(owner);
    setSize(350, 250);

    label = new JLabel();
    this.add(label);

    String helpText;
    try {
      InputStream helpStream = ClassLoader.getSystemResourceAsStream(
          "flood/" + helpFilename);
      if (helpStream == null) {
        JOptionPane.showMessageDialog(owner, "ERROR: Could not load '"
            + helpFilename + "'", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      helpText = IOUtils.toString(helpStream);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(owner, "ERROR: " + e.toString());
      return;
    }
    label.setText(helpText);

    this.setVisible(true);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

}
