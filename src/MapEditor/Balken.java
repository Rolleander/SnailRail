package MapEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Balken implements MouseMotionListener, MouseListener {

	private int xpos, ypos;
	private int weite, hoehe;
	private boolean editable = true;

	private int sx;

	public Balken(int x, int y, int w, int h) {
		xpos = x;
		ypos = y;
		weite = w;
		hoehe = h;
		sx = 0;
	}

	public void setSchieber(int s) {
		sx = s;
	}

	public int getSchieberWeite() {
		return sx;
	}

	public void setEditable(boolean b) {
		editable = b;
	}

	public void paint(Graphics g) {
		if (sx > weite) {
			sx = weite;
		}
		if (sx < 0) {
			sx = 0;
		}
		g.setColor(new Color(150, 150, 150));
		g.fillRect(xpos, ypos, weite, hoehe);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(xpos, ypos, weite, hoehe);
		g.setColor(new Color(100, 100, 100));
		g.fillRect(xpos + 2, ypos + 3, weite - 4, hoehe - 6);

		int proz = (int) (((double) sx / (double) weite) * 150);
		g.setColor(new Color(100 + proz / 2, 100 + proz, 100));
		g.fillRect(xpos + 2, ypos + 3, sx - 4, hoehe - 6);

		g.setColor(new Color(200, 200, 200));
		g.fillRect(sx - 5 + xpos, ypos - 5, 10, hoehe + 10);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(sx - 5 + xpos, ypos - 5, 10, hoehe + 10);

	}

	public void mouseDragged(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();

		if (mx >= sx - 5 + xpos && mx <= sx + 5 + xpos && my > ypos - 5 && my < ypos + hoehe + 10) {
			sx = mx - xpos;
		} else if (mx >= xpos && mx <= xpos + weite && my > ypos && my < ypos + hoehe) {
			sx = mx - xpos;
		}
	}

	public void mouseMoved(MouseEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		int mx = e.getX();
		int my = e.getY();
		if (mx >= xpos && mx <= xpos + weite && my > ypos && my < ypos + hoehe) {
			sx = mx - xpos;
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
