package util.swingutils;

import javax.swing.JOptionPane;

/**
 * To use this class, put {@link SwingUtils#useDialogExceptionHandler()} at the top
 * of your main() function.
 */
public class UncaughtExceptionHandler {
  public UncaughtExceptionHandler() {
    // empty
  }

  public void handle(Throwable t) {
    String message = "Error: " + t + "\nat " + t.getStackTrace()[0];
    JOptionPane.showMessageDialog(SwingUtils.getActiveFrame(), message,
        "Error", JOptionPane.ERROR_MESSAGE);
  }
}
