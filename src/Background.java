import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Background {

	private int breite, hoehe;

	private Image backs[] = new Image[6];

	public Background() {
		for (int i = 0; i < 6; i++) {
			URL filename = getClass().getResource("back" + i + ".png");
			backs[i] = Toolkit.getDefaultToolkit().getImage(filename);
		}
	}

	int art = -1;

	public void init(int nr) {
		art = (nr) / 5;
		if (nr == 12) {
			art = 5;
		}

	}

	public void paint(Graphics g, int x, int y) {
		g.drawImage(backs[art], 0, 0, null);
	}

}
