package flood;

import java.awt.Color;
import java.awt.event.MouseEvent;

import util.swingutils.DefaultMouseListener;

public class FlooditMouseListener extends DefaultMouseListener {

  private Floodit floodit;

  public FlooditMouseListener(Floodit floodit) {
    this.floodit = floodit;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    floodit.debug(e.getX() + " " + e.getY());
    int squareWidth = floodit.gridSize.width / floodit.grid.getWidth();
    int squareHeight = floodit.gridSize.height / floodit.grid.getHeight();
    int x = e.getX() / squareWidth;
    int y = e.getY() / squareHeight;
    Color color = floodit.grid.get(x, y).getColor();
    floodit.debug(color.toString());

    floodit.selectColor(color);
  }
}
