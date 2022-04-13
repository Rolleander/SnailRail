import java.awt.Graphics;

public class KnifeBlock extends MapObjekt {

	private int knives = 0;
	private int down = 0;

	public KnifeBlock(int dreh) {
		drehung = dreh;
		stand = true;
		climb = true;
	}

	private void checkeAction(int x, int y) {

		if (x + 10 >= pos[0] && x - 10 <= pos[0] + 40) {
			if (drehung < 2) {
				if (y - 5 >= pos[1] - knives && y + 5 <= pos[1]) {
					Main.schaden(false);
				}
			} else {
				if (y - 5 >= pos[1] + 40 && y + 5 <= pos[1] + 40 + knives) {
					Main.schaden(false);
				}
			}
		}
	}

	public void paint(Graphics g, int x, int y) {
		if (drehung < 2) {// hoch
			g.drawImage(Main.mapBilder[2], pos[0] - x + 480, pos[1] - y + 280 - knives, null);
		} else {// runter
			g.drawImage(Main.mapBilder[2], pos[0] - x + 480, pos[1] - y + 320 + knives, 40, -40, null);
		}
		switch (drehung) {
		case 0:
			g.drawImage(Main.mapBilder[86], pos[0] - x + 480, pos[1] - y + 280, null);
			break;
		case 1:
			g.drawImage(Main.mapBilder[86], pos[0] - x + 520, pos[1] - y + 280, -40, 40, null);
			break;
		case 2:
			g.drawImage(Main.mapBilder[86], pos[0] - x + 480, pos[1] - y + 320, 40, -40, null);
			break;
		case 3:
			g.drawImage(Main.mapBilder[86], pos[0] - x + 520, pos[1] - y + 320, -40, -40, null);
			break;
		}

		if (down < 100) {
			if (knives < 40) {
				knives++;
			} else {
				down++;
			}
		} else {
			if (knives > 0) {
				knives--;
			} else {
				down++;
				if (down > 200) {
					down = 0;
				}
			}
		}
		if (knives > 10) {
			checkeAction(x, y);
		}
	}

}
