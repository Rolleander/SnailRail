import java.awt.Graphics;

public class Brocken extends MapObjekt {

	private boolean fallen = false;
	private int fallh = 0;
	private int image;

	public Brocken(int id) {
		aktiv = true;
		image = id;
	}

	private void checkeAction(int x, int y) {
		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (y + 10 >= pos[1] && y - 10 <= pos[1] + 40) {
				aktiv = false;
				Main.sound.playSound(1);
				Main.schaden(false);
			}
		}
	}

	public void paint(Graphics g, int x, int y) {
		if (aktiv) {
			if (fallen == false) {
				if (x + 30 >= pos[0] && x - 30 <= pos[0] + 40 && y > pos[1] && y < pos[1] + 300) {
					fallen = true;
					Main.sound.playSound(2);
				}
			} else {
				pos[1] += 2 + fallh / 10;
				fallh++;
				checkeAction(x, y);
				if (pos[1] > 1000) {
					aktiv = false;
				}
			}

			g.drawImage(Main.mapBilder[image], pos[0] - x + 480, pos[1] - y + 280, null);
		}

	}
}
