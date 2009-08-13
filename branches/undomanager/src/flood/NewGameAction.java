package flood;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class NewGameAction extends AbstractAction {
  private Floodit floodit;

  public NewGameAction(Floodit floodit) {
    super("New game...");
    this.floodit = floodit;
    putValue(SHORT_DESCRIPTION, "Start a new game.");
    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
        ActionEvent.CTRL_MASK));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    new NewGameDialog(floodit);
  }
}