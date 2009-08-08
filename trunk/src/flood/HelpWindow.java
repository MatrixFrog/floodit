package flood;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.io.IOUtils;

class HelpWindow extends JDialog {

	public static void main(String[] args) {
		new HelpWindow(null, "help.htm");
	}

	JLabel label;

	HelpWindow(JFrame owner, String htmlFilename) {
		super(owner);
		setSize(350, 250);

		label = new JLabel();
		this.add(label);

		InputStream helpStream = getClass().getResourceAsStream(htmlFilename);

		try {
			String helpText = IOUtils.toString(helpStream);
			label.setText(helpText);
		} catch (IOException e) {
			label.setText(e.toString());
			//JOptionPane.showMessageDialog(owner, e.toString());
		}

		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

}
