package util.swingutils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class SwingUtils {
  private SwingUtils() {
  }

  // TODO this may only make sense for JFrames so maybe the argument type should be JFrame instead of Component?
  public static void centerOnScreen(Component component) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    component.setLocation(screenSize.width/2 - component.getWidth()/2,
        screenSize.height/2 - component.getHeight()/2);
  }

  public static void useDialogExceptionHandler() {
    System.setProperty("sun.awt.exception.handler", UncaughtExceptionHandler.class.getCanonicalName());
  }

  /**
   * "Wraps" a component in a JPanel. In layouts where components tend to stretch, this is
   * useful because the JPanel will stretch but the actual component will not.
   * @return A JPanel containing the specified component.
   */
  public static JPanel box(Component component) {
    JPanel panel = new JPanel();
    panel.add(component);
    return panel;
  }

  /**
   * @return a JPanel containing a label with the specified String as its text, and the component.
   */
  public static JPanel labelComponent(JComponent c, String s) {
    JPanel panel = new JPanel();
    panel.add(new JLabel(s));
    panel.add(c);
    return panel;
  }

  /**
   * Sets the application to use the look and feel that is standard for the system it is
   * running on. If any exceptions occur, it does nothing, so the ugly Java look is used instead.
   */
  public static void useDefaultLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      // Do nothing
    }
  }

  public static Frame getActiveFrame() {
    for (Frame frame : Frame.getFrames()) {
      if (frame.isActive()) {
        return frame;
      }
    }
    return null;
  }
}
