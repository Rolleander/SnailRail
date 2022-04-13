import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseAndKey implements MouseListener, MouseMotionListener, KeyListener {

	private boolean tasten[] = new boolean[8];

	public MouseAndKey() {
		for (int i = 0; i < tasten.length; i++) {
			tasten[i] = false;
		}
	}

	public boolean[] getTastenDruck() {
		return tasten;
	}

	public void clear() {
		tasten[4] = false;
		tasten[6] = false;
		tasten[7] = false;
	}

	public void mouseClicked(MouseEvent e) {
		// Wird aufgerufen wenn die Maus die Taste klickt und dann wieder losl�sst
	}

	public void mousePressed(MouseEvent e) {
		// Wird aufgerufen wenn eine Maustaste gedr�ckt wird
	}

	public void mouseReleased(MouseEvent e) {
		// Wird aufgerufen wenn eine Maustaste losgelassen wird

	}

	public void mouseEntered(MouseEvent e) {
		// Wird aufgerufen wenn die Maus einen Bereich betritt (zb. einen Button usw)

	}

	public void mouseExited(MouseEvent e) {
		// Wird aufgerufen wenn die Maus einen Bereich verl�sst (zb. einen Button usw)

	}

	public void mouseDragged(MouseEvent e) {
		// Wird aufgerufen wenn die Maus bei gedr�ckter Taste bewegt wid

	}

	public void mouseMoved(MouseEvent e) {
		// Wird aufgerufen wenn die Maus bewegt wird (ohne eine Taste gedr�ckt zu
		// halten)

	}

	public void druckTaste(int taste, boolean pressed) {
		switch (taste) {
		case KeyEvent.VK_LEFT:
			tasten[0] = pressed;
			break;
		case KeyEvent.VK_RIGHT:
			tasten[1] = pressed;
			break;
		case KeyEvent.VK_UP:
			tasten[2] = pressed;
			break;
		case KeyEvent.VK_DOWN:
			tasten[3] = pressed;
			break;
		case KeyEvent.VK_SPACE:
			tasten[4] = !pressed;
			break;
		case KeyEvent.VK_ESCAPE:
			tasten[5] = pressed;
			break;
		case KeyEvent.VK_X:
			tasten[6] = !pressed;
			break;
		case KeyEvent.VK_Y:
			tasten[7] = !pressed;
			break;
		}
	}

	public void keyPressed(KeyEvent k) {
		// Wird aufgerufen wenn eine Taste gedr�ckt wird
		druckTaste(k.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent k) {
		// Wird aufgerufen wenn eine Taste losgelassen wird
		druckTaste(k.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent k) {
		// Wird aufgerufen wenn eine Taste gedr�ckt und danach losgelassen wird

	}

}
