package flood;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

abstract class AbstractHelpAction extends AbstractAction {
  private JFrame window;
  private String name;

  public AbstractHelpAction(JFrame window, String name) {
    super(name);
    this.window = window;
    this.name = name;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    new HelpWindow(window, name + ".htm");
  }
}

class HelpAction extends AbstractHelpAction {
  public HelpAction(JFrame window) {
    super(window, "Help");
    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
  }
}

class AboutAction extends AbstractHelpAction {
  public AboutAction(JFrame window) {
    super(window, "About");
  }
}
